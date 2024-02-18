import unittest
from unittest.mock import patch, MagicMock

from flask import Flask

from app import barbeiros_blueprint


class TestBarbeiroRoutes(unittest.TestCase):
    def setUp(self):
        self.app = Flask(__name__)
        self.app.register_blueprint(barbeiros_blueprint)
        self.client = self.app.test_client()

    @patch('app.barbeiros.routes.get_db_connection')
    def test_get_barbeiros(self, mock_get_db_connection):
        mock_cursor = MagicMock()

        expected_data = [(1, '012345678910', 'Barbeiro Teste', 'barbeiro@teste.com', '88888888', False, 1)]
        mock_cursor.fetchall.return_value = expected_data

        mock_connection = MagicMock()
        mock_connection.cursor.return_value.__enter__.return_value = mock_cursor
        mock_get_db_connection.return_value = mock_connection

        response = self.client.get('/barbeiros')

        mock_get_db_connection.assert_called_once()
        mock_connection.cursor.assert_called_once()
        mock_cursor.execute.assert_called_once_with("SELECT * FROM barbeiros")

        expected_result = {
            "barbeiros": [
                {
                    'id': 1,
                    'cpf': '012345678910',
                    'nome': 'Barbeiro Teste',
                    'email': 'barbeiro@teste.com',
                    'celular': '88888888',
                    'admin': False,
                    'idBarbearia': 1
                }
            ]
        }

        self.assertDictEqual(response.json, expected_result)

    @patch('app.barbeiros.routes.get_db_connection')
    def test_get_barbeiro_by_id(self, mock_get_db_connection):
        mock_cursor = MagicMock()
        mock_connection = MagicMock()
        mock_connection.cursor.return_value.__enter__.return_value = mock_cursor
        mock_get_db_connection.return_value = mock_connection

        barbeiro_id = '1'
        expected_data = (1, '012345678910', 'Barbeiro Teste', 'barbeiro@teste.com', '88888888', False, 1)
        mock_cursor.fetchone.return_value = expected_data

        response = self.client.get(f'/barbeiros/{barbeiro_id}')

        mock_get_db_connection.assert_called_once()
        mock_connection.cursor.assert_called_once()
        mock_cursor.execute.assert_called_once_with("SELECT * FROM barbeiros WHERE id = %s", (barbeiro_id,))
        mock_cursor.fetchone.assert_called_once()

        expected_result = {
            'barbeiro': {
                'id': 1,
                'cpf': '012345678910',
                'nome': 'Barbeiro Teste',
                'email': 'barbeiro@teste.com',
                'celular': '88888888',
                'admin': False,
                'idBarbearia': 1
            }
        }

        self.assertEqual(response.status_code, 200)
        self.assertDictEqual(response.json, expected_result)
