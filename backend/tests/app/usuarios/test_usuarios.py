import unittest
from unittest.mock import patch, MagicMock, ANY, call

from flask import Flask

from app import usuarios_blueprint


class TestUsuarioRoutes(unittest.TestCase):
    def setUp(self):
        self.app = Flask(__name__)
        self.app.register_blueprint(usuarios_blueprint)
        self.client = self.app.test_client()

    @patch('app.usuarios.routes.get_db_connection')
    def test_get_usuarios(self, mock_get_db_connection):
        mock_cursor = MagicMock()

        expected_data = [
            ('adb39f57-9b5a-4859-bf9f-b5bb8f1f1bb3', '123456789', 'Usuário Teste', '01/01/2001', 'email@teste.com',
             '12345678')]
        mock_cursor.fetchall.return_value = expected_data

        mock_connection = MagicMock()
        mock_connection.cursor.return_value.__enter__.return_value = mock_cursor
        mock_get_db_connection.return_value = mock_connection

        response = self.client.get('/usuarios')

        mock_get_db_connection.assert_called_once()
        mock_connection.cursor.assert_called_once()
        mock_cursor.execute.assert_called_once_with("SELECT * FROM usuarios")

        expected_result = {
            'usuarios': [
                {
                    'id': 'adb39f57-9b5a-4859-bf9f-b5bb8f1f1bb3',
                    'cpf': '123456789',
                    'nome': 'Usuário Teste',
                    'dataNascimento': '01/01/2001',
                    'email': 'email@teste.com',
                    'celular': '12345678'
                }
            ]
        }

        self.assertDictEqual(response.json, expected_result)
        self.assertEqual(response.status_code, 200)

    @patch('app.usuarios.routes.get_db_connection')
    def test_get_usuario_by_id(self, mock_get_db_connection):
        mock_cursor = MagicMock()

        expected_data = (
            'adb39f57-9b5a-4859-bf9f-b5bb8f1f1bb3', '123456789', 'Usuário Teste', '01/01/2001', 'email@teste.com',
            '12345678')
        mock_cursor.fetchone.return_value = expected_data

        mock_connection = MagicMock()
        mock_connection.cursor.return_value.__enter__.return_value = mock_cursor
        mock_get_db_connection.return_value = mock_connection

        usuario_id = 'adb39f57-9b5a-4859-bf9f-b5bb8f1f1bb3'
        response = self.client.get(f'/usuarios/{usuario_id}')

        mock_get_db_connection.assert_called_once()
        mock_connection.cursor.assert_called_once()
        mock_cursor.execute.assert_called_once_with("SELECT * FROM usuarios WHERE id = %s", (usuario_id,))
        mock_cursor.fetchone.assert_called_once()

        expected_result = {
            'id': 'adb39f57-9b5a-4859-bf9f-b5bb8f1f1bb3',
            'cpf': '123456789',
            'nome': 'Usuário Teste',
            'dataNascimento': '01/01/2001',
            'email': 'email@teste.com',
            'celular': '12345678'
        }

        self.assertDictEqual(response.json, expected_result)
        self.assertEqual(response.status_code, 200)

    @patch('app.usuarios.routes.get_db_connection')
    def test_get_usuario_by_id_not_found(self, mock_get_db_connection):
        mock_cursor = MagicMock()
        mock_cursor.fetchone.return_value = None

        mock_connection = MagicMock()
        mock_connection.cursor.return_value.__enter__.return_value = mock_cursor
        mock_get_db_connection.return_value = mock_connection

        usuario_id = 'adb39f57-9b5a-4859-bf9f-b5bb8f1f1bb3'
        response = self.client.get(f'/usuarios/{usuario_id}')

        mock_get_db_connection.assert_called_once()
        mock_connection.cursor.assert_called_once()
        mock_cursor.execute.assert_called_once_with("SELECT * FROM usuarios WHERE id = %s", (usuario_id,))
        mock_cursor.fetchone.assert_called_once()

        expected_result = {"mensagem": "Usuário não encontrado."}

        self.assertEqual(response.status_code, 404)
        self.assertDictEqual(response.json, expected_result)

    @patch('app.usuarios.routes.get_db_connection')
    def test_update_usuario_by_id(self, mock_get_db_connection):
        mock_cursor = MagicMock()
        mock_connection = MagicMock()
        mock_connection.cursor.return_value.__enter__.return_value = mock_cursor
        mock_get_db_connection.return_value = mock_connection

        usuario_id = 'adb39f57-9b5a-4859-bf9f-b5bb8f1f1bb3'
        dados_atualizados = {
            'nome': 'Usuário Atualizado',
            'endereco': 'Novo Endereço',
            'email': 'novo_email@example.com',
            'celular': '987654321'
        }
        response = self.client.put(f'/usuarios/{usuario_id}', json=dados_atualizados)

        mock_get_db_connection.assert_called_once()
        mock_connection.cursor.assert_called_once()
        mock_cursor.execute.assert_has_calls([
            call("SELECT * FROM usuarios WHERE id = %s", (usuario_id,)),
            call('UPDATE usuarios SET celular = %s WHERE id = %s', ('987654321', usuario_id)),
            call('UPDATE usuarios SET email = %s WHERE id = %s', ('novo_email@example.com', usuario_id)),
            call('UPDATE usuarios SET endereco = %s WHERE id = %s', ('Novo Endereço', usuario_id)),
            call('UPDATE usuarios SET nome = %s WHERE id = %s', ('Usuário Atualizado', usuario_id))
        ])

        self.assertEqual(response.status_code, 200)
        expected_result = {"mensagem": "Usuário atualizado com sucesso!"}
        self.assertDictEqual(response.json, expected_result)

    @patch('app.usuarios.routes.get_db_connection')
    def test_delete_usuario_by_id(self, mock_get_db_connection):
        mock_cursor = MagicMock()
        mock_connection = MagicMock()
        mock_connection.cursor.return_value.__enter__.return_value = mock_cursor
        mock_get_db_connection.return_value = mock_connection

        usuario_id = 'adb39f57-9b5a-4859-bf9f-b5bb8f1f1bb3'
        response = self.client.delete(f'/usuarios/{usuario_id}')

        mock_get_db_connection.assert_called_once()
        mock_cursor.execute.assert_called_once_with("DELETE FROM usuarios WHERE id = %s", (usuario_id,))

        self.assertEqual(response.status_code, 204)
