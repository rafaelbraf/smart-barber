import unittest
from unittest.mock import patch, MagicMock, call

from flask import Flask

from app import servicos_blueprint


class TestServicoRoutes(unittest.TestCase):
    def setUp(self):
        self.app = Flask(__name__)
        self.app.register_blueprint(servicos_blueprint)
        self.client = self.app.test_client()

    @patch('app.servicos.routes.get_db_connection')
    def test_get_servicos(self, mock_get_db_connection):
        mock_cursor = MagicMock()

        expected_data = [(1, 'Serviço Teste', 20.0, 20, 1)]
        mock_cursor.fetchall.return_value = expected_data

        mock_connection = MagicMock()
        mock_connection.cursor.return_value.__enter__.return_value = mock_cursor
        mock_get_db_connection.return_value = mock_connection

        response = self.client.get('/servicos')

        mock_get_db_connection.assert_called_once()
        mock_connection.cursor.assert_called_once()
        mock_cursor.execute.assert_called_once_with("SELECT * FROM servicos")

        expected_result = {
            "servicos": [
                {
                    'id': 1,
                    'nome': 'Serviço Teste',
                    'preco': 20.0,
                    'tempoDuracaoEmMinutos': 20,
                    'idBarbearia': 1,
                }
            ]
        }

        self.assertEqual(response.status_code, 200)
        self.assertDictEqual(response.json, expected_result)

    @patch('app.servicos.routes.get_db_connection')
    def test_get_servico_by_id(self, mock_get_db_connection):
        mock_cursor = MagicMock()
        mock_connection = MagicMock()
        mock_connection.cursor.return_value.__enter__.return_value = mock_cursor
        mock_get_db_connection.return_value = mock_connection

        servico_id = '1'
        expected_data = (1, 'Serviço Teste', 20.0, 20, 1)
        mock_cursor.fetchone.return_value = expected_data

        response = self.client.get(f'/servicos/{servico_id}')

        mock_get_db_connection.assert_called_once()
        mock_connection.cursor.assert_called_once()
        mock_cursor.execute.assert_called_once_with("SELECT * FROM servicos WHERE id = %s", (servico_id,))
        mock_cursor.fetchone.assert_called_once()

        expected_result = {
            'id': 1,
            'nome': 'Serviço Teste',
            'preco': 20.0,
            'tempoDuracaoEmMinutos': 20,
            'idBarbearia': 1
        }

        self.assertEqual(response.status_code, 200)
        self.assertDictEqual(response.json, expected_result)

    @patch('app.servicos.routes.get_db_connection')
    def test_get_servico_by_id_not_found(self, mock_get_db_connection):
        mock_cursor = MagicMock()
        mock_cursor.fetchone.return_value = None

        mock_connection = MagicMock()
        mock_connection.cursor.return_value.__enter__.return_value = mock_cursor
        mock_get_db_connection.return_value = mock_connection

        servico_id = '999'
        response = self.client.get(f'/servicos/{servico_id}')

        mock_get_db_connection.assert_called_once()
        mock_connection.cursor.assert_called_once()
        mock_cursor.execute.assert_called_once_with("SELECT * FROM servicos WHERE id = %s", (servico_id,))
        mock_cursor.fetchone.assert_called_once()

        expected_result = {"mensagem": "Serviço não encontrado."}

        self.assertEqual(response.status_code, 404)
        self.assertDictEqual(response.json, expected_result)

    @patch('app.servicos.routes.get_db_connection')
    def test_get_servico_by_barbearia_id(self, mock_get_db_connection):
        mock_cursor = MagicMock()

        expected_data = [(1, 'Serviço Teste', 20.0, 20, 1)]
        mock_cursor.fetchall.return_value = expected_data

        mock_connection = MagicMock()
        mock_connection.cursor.return_value.__enter__.return_value = mock_cursor
        mock_get_db_connection.return_value = mock_connection

        barbearia_id = '1'
        response = self.client.get(f'/servicos/barbearia/{barbearia_id}')

        mock_get_db_connection.assert_called_once()
        mock_connection.cursor.assert_called_once()
        mock_cursor.execute.assert_called_once_with("SELECT * FROM servicos WHERE id_barbearia = %s", (barbearia_id,))

        expected_result = {
            "servicos": [
                {
                    'id': 1,
                    'nome': 'Serviço Teste',
                    'preco': 20.0,
                    'tempoDuracaoEmMinutos': 20,
                    'idBarbearia': 1,
                }
            ]
        }

        self.assertEqual(response.status_code, 200)
        self.assertDictEqual(response.json, expected_result)

    @patch('app.servicos.routes.get_db_connection')
    def test_insert_servico(self, mock_get_db_connection):
        mock_cursor = MagicMock()
        mock_cursor.execute.return_value.fetchone.return_value = (45,)

        mock_connection = MagicMock()
        mock_connection.cursor.return_value.__enter__.return_value = mock_cursor
        mock_get_db_connection.return_value = mock_connection

        novo_servico = {
            'nome': 'Novo Serviço Teste',
            'preco': 20,
            'tempoDuracaoEmMinutos': 20,
            'idBarbearia': 1
        }
        response = self.client.post('/servicos', json=novo_servico)

        mock_get_db_connection.assert_called_once()
        mock_cursor.execute.assert_called_once_with(
            "INSERT INTO servicos (nome, preco, tempo_duracao_em_minutos, id_barbearia) VALUES (%s, "
            "%s, %s, %s) RETURNING id",
            (novo_servico['nome'], novo_servico['preco'], novo_servico['tempoDuracaoEmMinutos'],
             novo_servico['idBarbearia'])
        )

        self.assertEqual(response.status_code, 201)

    @patch('app.servicos.routes.get_db_connection')
    def test_update_servico_by_id(self, mock_get_db_connection):
        mock_cursor = MagicMock()
        mock_connection = MagicMock()
        mock_connection.cursor.return_value.__enter__.return_value = mock_cursor
        mock_get_db_connection.return_value = mock_connection

        servico_id = '1'
        dados_atualizados = {
            'nome': 'Serviço Atualizado Teste',
            'preco': 20,
            'tempoDuracaoEmMinutos': 20,
        }
        response = self.client.put(f'/servicos/{servico_id}', json=dados_atualizados)

        mock_get_db_connection.assert_called_once()
        mock_connection.cursor.assert_called_once()
        mock_cursor.execute.assert_has_calls([
            call('SELECT * FROM servicos WHERE id = %s', ('1',)),
            call('UPDATE servicos SET nome = %s WHERE id = %s', ('Serviço Atualizado Teste', '1')),
            call('UPDATE servicos SET preco = %s WHERE id = %s', (20, '1')),
            call('UPDATE servicos SET tempo_duracao_em_minutos = %s WHERE id = %s', (20, '1'))
        ])

        self.assertEqual(response.status_code, 200)
        expected_result = {"mensagem": "Serviço atualizado com sucesso!"}
        self.assertDictEqual(response.json, expected_result)

    @patch('app.servicos.routes.get_db_connection')
    def test_delete_servico_by_id(self, mock_get_db_connection):
        mock_cursor = MagicMock()
        mock_connection = MagicMock()
        mock_connection.cursor.return_value.__enter__.return_value = mock_cursor
        mock_get_db_connection.return_value = mock_connection

        servico_id = '999'
        response = self.client.delete(f'/servicos/{servico_id}')

        mock_get_db_connection.assert_called_once()
        mock_cursor.execute.assert_called_once_with("DELETE FROM servicos WHERE id = %s", (servico_id,))

        self.assertEqual(response.status_code, 204)
