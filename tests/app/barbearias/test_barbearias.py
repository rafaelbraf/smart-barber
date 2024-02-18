import unittest
from unittest.mock import patch, MagicMock, call

from flask import Flask

from app.barbearias import barbearias_blueprint


class TestBarbeariaRoutes(unittest.TestCase):
    def setUp(self):
        self.app = Flask(__name__)
        self.app.register_blueprint(barbearias_blueprint)
        self.client = self.app.test_client()

    @patch('app.barbearias.routes.get_db_connection')
    def test_get_barbearias(self, mock_get_db_connection):
        mock_cursor = MagicMock()

        expected_data = [(1, '123456789', 'Barbearia Teste', 'Endereço Teste', 'email@teste.com', '123456789')]
        mock_cursor.fetchall.return_value = expected_data

        mock_connection = MagicMock()
        mock_connection.cursor.return_value.__enter__.return_value = mock_cursor
        mock_get_db_connection.return_value = mock_connection

        response = self.client.get('/barbearias')

        mock_get_db_connection.assert_called_once()
        mock_connection.cursor.assert_called_once()
        mock_cursor.execute.assert_called_once_with("SELECT * FROM barbearias")

        expected_result = {
            'barbearias': [
                {
                    'id': 1,
                    'cnpj': '123456789',
                    'nome': 'Barbearia Teste',
                    'endereco': 'Endereço Teste',
                    'email': 'email@teste.com',
                    'telefone': '123456789'
                }
            ]
        }

        self.assertDictEqual(response.json, expected_result)

    @patch('app.barbearias.routes.get_db_connection')
    def test_get_barbearia_by_id(self, mock_get_db_connection):
        mock_cursor = MagicMock()

        expected_data = (1, '123456789', 'Barbearia Teste', 'Endereço Teste', 'email@teste.com', '123456789')
        mock_cursor.fetchone.return_value = expected_data

        mock_connection = MagicMock()
        mock_connection.cursor.return_value.__enter__.return_value = mock_cursor
        mock_get_db_connection.return_value = mock_connection

        barbearia_id = '1'
        response = self.client.get(f'/barbearias/{barbearia_id}')

        mock_get_db_connection.assert_called_once()
        mock_connection.cursor.assert_called_once()
        mock_cursor.execute.assert_called_once_with("SELECT * FROM barbearias WHERE id = %s", (barbearia_id,))
        mock_cursor.fetchone.assert_called_once()

        expected_result = {
            'id': 1,
            'cnpj': '123456789',
            'nome': 'Barbearia Teste',
            'endereco': 'Endereço Teste',
            'email': 'email@teste.com',
            'telefone': '123456789'
        }

        self.assertDictEqual(response.json, expected_result)

    @patch('app.barbearias.routes.get_db_connection')
    def test_get_barbearia_by_id_not_found(self, mock_get_db_connection):
        mock_cursor = MagicMock()
        mock_cursor.fetchone.return_value = None

        mock_connection = MagicMock()
        mock_connection.cursor.return_value.__enter__.return_value = mock_cursor
        mock_get_db_connection.return_value = mock_connection

        barbearia_id = '999'
        response = self.client.get(f'/barbearias/{barbearia_id}')

        mock_get_db_connection.assert_called_once()
        mock_connection.cursor.assert_called_once()
        mock_cursor.execute.assert_called_once_with("SELECT * FROM barbearias WHERE id = %s", (barbearia_id,))
        mock_cursor.fetchone.assert_called_once()

        expected_result = {"mensagem": "Barbearia não encontrada."}

        self.assertEqual(response.json, expected_result)

    @patch('app.barbearias.routes.get_db_connection')
    def test_insert_barbearia(self, mock_get_db_connection):
        mock_cursor = MagicMock()
        mock_cursor.execute.return_value.fetchone.return_value = (42,)

        mock_connection = MagicMock()
        mock_connection.cursor.return_value.__enter__.return_value = mock_cursor
        mock_get_db_connection.return_value = mock_connection

        nova_barbearia = {
            'cnpj': '12345678901234',
            'nome': 'Nova Barbearia',
            'endereco': 'Endereço da Nova Barbearia',
            'email': 'nova_barbearia@example.com',
            'telefone': '987654321',
            'senha': 'senha_secreta'
        }
        response = self.client.post('/barbearias', json=nova_barbearia)

        mock_get_db_connection.assert_called_once()
        mock_cursor.execute.assert_called_once_with(
            "INSERT INTO barbearias (cnpj, nome, endereco, email, telefone, senha) VALUES (%s, %s, %s, %s, %s, "
            "%s) RETURNING id",
            (nova_barbearia['cnpj'], nova_barbearia['nome'], nova_barbearia['endereco'],
             nova_barbearia['email'], nova_barbearia['telefone'], nova_barbearia['senha'])
        )

        self.assertEqual(response.status_code, 201)

    @patch('app.barbearias.routes.get_db_connection')
    def test_update_barbearia_by_id(self, mock_get_db_connection):
        mock_cursor = MagicMock()
        mock_connection = MagicMock()
        mock_connection.cursor.return_value.__enter__.return_value = mock_cursor
        mock_get_db_connection.return_value = mock_connection

        barbearia_id = '1'
        dados_atualizados = {
            'cnpj': '12345678901234',
            'nome': 'Barbearia Atualizada',
            'endereco': 'Novo Endereço',
            'email': 'novo_email@example.com',
            'telefone': '987654321'
        }
        response = self.client.put(f'/barbearias/{barbearia_id}', json=dados_atualizados)

        mock_get_db_connection.assert_called_once()
        mock_connection.cursor.assert_called_once()
        mock_cursor.execute.assert_has_calls([
            call("SELECT * FROM barbearias WHERE id = %s", (barbearia_id,)),
            call('UPDATE barbearias SET cnpj = %s WHERE id = %s', ('12345678901234', '1')),
            call('UPDATE barbearias SET email = %s WHERE id = %s', ('novo_email@example.com', '1')),
            call('UPDATE barbearias SET endereco = %s WHERE id = %s', ('Novo Endereço', '1')),
            call('UPDATE barbearias SET nome = %s WHERE id = %s', ('Barbearia Atualizada', '1')),
            call('UPDATE barbearias SET telefone = %s WHERE id = %s', ('987654321', '1'))
        ])

        self.assertEqual(response.status_code, 200)
        expected_result = {"mensagem": "Barbearia atualizada com sucesso!"}
        self.assertDictEqual(response.json, expected_result)

    @patch('app.barbearias.routes.get_db_connection')
    def test_delete_barbearia_by_id(self, mock_get_db_connection):
        mock_cursor = MagicMock()
        mock_connection = MagicMock()
        mock_connection.cursor.return_value.__enter__.return_value = mock_cursor
        mock_get_db_connection.return_value = mock_connection

        barbearia_id = '999'
        response = self.client.delete(f'/barbearias/{barbearia_id}')

        mock_get_db_connection.assert_called_once()
        mock_cursor.execute.assert_called_once_with("DELETE FROM barbearias WHERE id = %s", (barbearia_id,))

        self.assertEqual(response.status_code, 204)
