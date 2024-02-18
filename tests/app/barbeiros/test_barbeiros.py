import unittest
from unittest.mock import patch, MagicMock, call

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

    @patch('app.barbeiros.routes.get_db_connection')
    def test_insert_barbeiro(self, mock_get_db_connection):
        mock_cursor = MagicMock()
        mock_cursor.execute.return_value.fetchone.return_value = (45,)

        mock_connection = MagicMock()
        mock_connection.cursor.return_value.__enter__.return_value = mock_cursor
        mock_get_db_connection.return_value = mock_connection

        novo_barbeiro = {
            'cpf': '12345678901234',
            'nome': 'Novo Barbeiro',
            'email': 'novo_barbeiro@example.com',
            'celular': '987654321',
            'admin': False,
            'idBarbearia': 1
        }
        response = self.client.post('/barbeiros', json=novo_barbeiro)

        mock_get_db_connection.assert_called_once()
        mock_cursor.execute.assert_called_once_with(
            "INSERT INTO barbeiros (cpf, nome, email, celular, admin, id_barbearia) VALUES (%s, %s, "
            "%s, %s, %s, %s) RETURNING id",
            (novo_barbeiro['cpf'], novo_barbeiro['nome'], novo_barbeiro['email'],
             novo_barbeiro['celular'], novo_barbeiro['admin'], novo_barbeiro['idBarbearia'])
        )

        self.assertEqual(response.status_code, 201)

    @patch('app.barbeiros.routes.get_db_connection')
    def test_update_barbeiro_by_id(self, mock_get_db_connection):
        mock_cursor = MagicMock()
        mock_connection = MagicMock()
        mock_connection.cursor.return_value.__enter__.return_value = mock_cursor
        mock_get_db_connection.return_value = mock_connection

        barbeiro_id = '1'
        dados_atualizados = {
            'cpf': '12345678901234',
            'nome': 'Barbeiro Atualizado',
            'email': 'novo_email@example.com',
            'celular': '987654321'
        }
        response = self.client.put(f'/barbeiros/{barbeiro_id}', json=dados_atualizados)

        mock_get_db_connection.assert_called_once()
        mock_connection.cursor.assert_called_once()
        mock_cursor.execute.assert_has_calls([
            call('SELECT * FROM barbeiros WHERE id = %s', ('1',)),
            call('UPDATE barbeiros SET celular = %s WHERE id = %s', ('987654321', '1')),
            call('UPDATE barbeiros SET cpf = %s WHERE id = %s', ('12345678901234', '1')),
            call('UPDATE barbeiros SET email = %s WHERE id = %s', ('novo_email@example.com', '1')),
            call('UPDATE barbeiros SET nome = %s WHERE id = %s', ('Barbeiro Atualizado', '1'))
        ])

        self.assertEqual(response.status_code, 200)
        expected_result = {"mensagem": "Barbeiro atualizado com sucesso!"}
        self.assertDictEqual(response.json, expected_result)

    @patch('app.barbeiros.routes.get_db_connection')
    def test_delete_barbeiro_by_id(self, mock_get_db_connection):
        mock_cursor = MagicMock()
        mock_connection = MagicMock()
        mock_connection.cursor.return_value.__enter__.return_value = mock_cursor
        mock_get_db_connection.return_value = mock_connection

        barbeiro_id = '999'
        response = self.client.delete(f'/barbeiros/{barbeiro_id}')

        mock_get_db_connection.assert_called_once()
        mock_cursor.execute.assert_called_once_with("DELETE FROM barbeiros WHERE id = %s", (barbeiro_id,))

        self.assertEqual(response.status_code, 204)
