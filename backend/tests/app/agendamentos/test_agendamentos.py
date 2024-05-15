import unittest
from unittest.mock import patch, MagicMock, call, ANY

from flask import Flask

from app import agendamentos_blueprint

DATA_AGENDAMENTO = '20/02/2024'


class TestAgendamentoRoutes(unittest.TestCase):
    def setUp(self):
        self.app = Flask(__name__)
        self.app.register_blueprint(agendamentos_blueprint)
        self.client = self.app.test_client()

    @patch('app.agendamentos.routes.get_db_connection')
    def test_get_agendamentos(self, mock_get_db_connection):
        mock_cursor = MagicMock()

        expected_data = [('adb39f57-9b5a-4859-bf9f-b5bb8f1f1bb3', DATA_AGENDAMENTO, '18:22', 50.0, 40, 1, 1)]
        mock_cursor.fetchall.return_value = expected_data

        mock_connection = MagicMock()
        mock_connection.cursor.return_value.__enter__.return_value = mock_cursor
        mock_get_db_connection.return_value = mock_connection

        response = self.client.get('/agendamentos')

        mock_get_db_connection.assert_called_once()
        mock_connection.cursor.assert_called_once()
        mock_cursor.execute.assert_called_once_with("SELECT * FROM agendamentos")

        expected_result = {
            "agendamentos": [
                {
                    'id': 'adb39f57-9b5a-4859-bf9f-b5bb8f1f1bb3',
                    'data': DATA_AGENDAMENTO,
                    'hora': '18:22',
                    'valorTotal': 50.0,
                    'tempoDuracaoEmMinutos': 40,
                    'idBarbearia': 1,
                    'idUsuario': 1
                }
            ]
        }

        self.assertEqual(response.status_code, 200)
        self.assertDictEqual(response.json, expected_result)

    @patch('app.agendamentos.routes.get_db_connection')
    def test_get_agendamento_by_id(self, mock_get_db_connection):
        mock_cursor = MagicMock()
        mock_connection = MagicMock()
        mock_connection.cursor.return_value.__enter__.return_value = mock_cursor
        mock_get_db_connection.return_value = mock_connection

        agendamento_id = 'adb39f57-9b5a-4859-bf9f-b5bb8f1f1bb3'
        expected_data = ('adb39f57-9b5a-4859-bf9f-b5bb8f1f1bb3', DATA_AGENDAMENTO, '18:22', 50.0, 40, 1, 1)
        mock_cursor.fetchone.return_value = expected_data

        response = self.client.get(f'/agendamentos/{agendamento_id}')

        mock_get_db_connection.assert_called_once()
        mock_connection.cursor.assert_called_once()
        mock_cursor.execute.assert_called_once_with("SELECT * FROM agendamentos WHERE id = %s", (agendamento_id,))
        mock_cursor.fetchone.assert_called_once()

        expected_result = {
            'id': 'adb39f57-9b5a-4859-bf9f-b5bb8f1f1bb3',
            'data': DATA_AGENDAMENTO,
            'hora': '18:22',
            'valorTotal': 50.0,
            'tempoDuracaoEmMinutos': 40,
            'idBarbearia': 1,
            'idUsuario': 1
        }

        self.assertEqual(response.status_code, 200)
        self.assertDictEqual(response.json, expected_result)

    @patch('app.agendamentos.routes.get_db_connection')
    def test_get_agendamento_by_id_not_found(self, mock_get_db_connection):
        mock_cursor = MagicMock()
        mock_cursor.fetchone.return_value = None

        mock_connection = MagicMock()
        mock_connection.cursor.return_value.__enter__.return_value = mock_cursor
        mock_get_db_connection.return_value = mock_connection

        agendamento_id = 'adb39f57-9b5a-4859-bf9f-b5bb8f1f1bb3'
        response = self.client.get(f'/agendamentos/{agendamento_id}')

        mock_get_db_connection.assert_called_once()
        mock_connection.cursor.assert_called_once()
        mock_cursor.execute.assert_called_once_with("SELECT * FROM agendamentos WHERE id = %s", (agendamento_id,))
        mock_cursor.fetchone.assert_called_once()

        expected_result = {"mensagem": "Agendamento não encontrado."}

        self.assertEqual(response.status_code, 404)
        self.assertDictEqual(response.json, expected_result)

    @patch('app.agendamentos.routes.get_db_connection')
    def test_get_agendamento_by_barbearia_id(self, mock_get_db_connection):
        mock_cursor = MagicMock()

        expected_data = [('adb39f57-9b5a-4859-bf9f-b5bb8f1f1bb3', DATA_AGENDAMENTO, '18:22', 50.0, 40, 1, 1)]
        mock_cursor.fetchall.return_value = expected_data

        mock_connection = MagicMock()
        mock_connection.cursor.return_value.__enter__.return_value = mock_cursor
        mock_get_db_connection.return_value = mock_connection

        barbearia_id = '1'
        response = self.client.get(f'/agendamentos/barbearia/{barbearia_id}')

        mock_get_db_connection.assert_called_once()
        mock_connection.cursor.assert_called_once()
        mock_cursor.execute.assert_called_once_with("SELECT * FROM agendamentos WHERE id_barbearia = %s",
                                                    (barbearia_id,))

        expected_result = {
            "agendamentos": [
                {
                    'id': 'adb39f57-9b5a-4859-bf9f-b5bb8f1f1bb3',
                    'data': DATA_AGENDAMENTO,
                    'hora': '18:22',
                    'valorTotal': 50.0,
                    'tempoDuracaoEmMinutos': 40,
                    'idBarbearia': 1,
                    'idUsuario': 1
                }
            ]
        }

        self.assertEqual(response.status_code, 200)
        self.assertDictEqual(response.json, expected_result)

    @patch('app.agendamentos.routes.get_db_connection')
    def test_insert_agendamento(self, mock_get_db_connection):
        agendamento_id = 'adb39f57-9b5a-4859-bf9f-b5bb8f1f1bb3'
        mock_cursor = MagicMock()
        mock_cursor.execute.return_value.fetchone.return_value = (agendamento_id,)

        mock_connection = MagicMock()
        mock_connection.cursor.return_value.__enter__.return_value = mock_cursor
        mock_get_db_connection.return_value = mock_connection

        novo_agendamento = {
            'data': DATA_AGENDAMENTO,
            'hora': '20:00',
            'valorTotal': 50.0,
            'tempoDuracaoEmMinutos': 40,
            'idBarbearia': 1,
            'idUsuario': 1,
            'servicos': [1, 2],
            'barbeiros': [1]
        }
        response = self.client.post('/agendamentos', json=novo_agendamento)

        mock_get_db_connection.assert_called_once()
        mock_cursor.execute.assert_has_calls([
            call("INSERT INTO agendamentos (data, hora, valor_total, tempo_duracao_em_minutos, "
                 "id_barbearia, id_usuario) VALUES (%s, %s, %s, %s, %s, %s) RETURNING id",
                 (DATA_AGENDAMENTO, '20:00', 50.0, 40, 1, 1)),
            call("INSERT INTO agendamento_servicos (id_agendamento, id_servico) VALUES (%s, %s)", (ANY, 1)),
            call("INSERT INTO agendamento_servicos (id_agendamento, id_servico) VALUES (%s, %s)", (ANY, 2)),
            call("INSERT INTO agendamento_barbeiros (id_agendamento, id_barbeiro) VALUES (%s, %s)", (ANY, 1))
        ])

        self.assertEqual(response.status_code, 201)

    @patch('app.agendamentos.routes.get_db_connection')
    def test_update_agendamento_by_id(self, mock_get_db_connection):
        mock_cursor = MagicMock()
        mock_connection = MagicMock()
        mock_connection.cursor.return_value.__enter__.return_value = mock_cursor
        mock_get_db_connection.return_value = mock_connection

        agendamento_id = 'adb39f57-9b5a-4859-bf9f-b5bb8f1f1bb3'
        dados_atualizados = {
            'data': DATA_AGENDAMENTO,
            'hora': '20:00',
            'valor_total': 50.0,
            'tempo_duracao_em_minutos': 40
        }
        response = self.client.put(f'/agendamentos/{agendamento_id}', json=dados_atualizados)

        mock_get_db_connection.assert_called_once()
        mock_connection.cursor.assert_called_once()
        mock_cursor.execute.assert_has_calls([
            call('SELECT * FROM agendamentos WHERE id = %s', ('adb39f57-9b5a-4859-bf9f-b5bb8f1f1bb3',)),
            call('UPDATE agendamentos SET data = %s WHERE id = %s', (DATA_AGENDAMENTO, 'adb39f57-9b5a-4859-bf9f'
                                                                                       '-b5bb8f1f1bb3')),
            call('UPDATE agendamentos SET hora = %s WHERE id = %s', ('20:00', 'adb39f57-9b5a-4859-bf9f-b5bb8f1f1bb3')),
            call('UPDATE agendamentos SET tempo_duracao_em_minutos = %s WHERE id = %s', (40, 'adb39f57-9b5a-4859-bf9f'
                                                                                             '-b5bb8f1f1bb3')),
            call('UPDATE agendamentos SET valor_total = %s WHERE id = %s', (50.0, 'adb39f57-9b5a-4859-bf9f'
                                                                                  '-b5bb8f1f1bb3')),
        ])

        self.assertEqual(response.status_code, 200)
        expected_result = {"mensagem": "Agendamento atualizado com sucesso!"}
        self.assertDictEqual(response.json, expected_result)

    @patch('app.agendamentos.routes.get_db_connection')
    def test_delete_agendamento_by_id(self, mock_get_db_connection):
        mock_cursor = MagicMock()
        mock_connection = MagicMock()
        mock_connection.cursor.return_value.__enter__.return_value = mock_cursor
        mock_get_db_connection.return_value = mock_connection

        agendamento_id = 'adb39f57-9b5a-4859-bf9f-b5bb8f1f1bb3'
        response = self.client.delete(f'/agendamentos/{agendamento_id}')

        mock_get_db_connection.assert_called_once()
        mock_cursor.execute.assert_has_calls([
            call('DELETE FROM agendamento_servicos WHERE id_agendamento = %s',
                 ('adb39f57-9b5a-4859-bf9f-b5bb8f1f1bb3',)),
            call('DELETE FROM agendamento_barbeiros WHERE id_agendamento = %s',
                 ('adb39f57-9b5a-4859-bf9f-b5bb8f1f1bb3',)),
            call('DELETE FROM agendamentos WHERE id = %s', ('adb39f57-9b5a-4859-bf9f-b5bb8f1f1bb3',))
        ])

        self.assertEqual(response.status_code, 204)
