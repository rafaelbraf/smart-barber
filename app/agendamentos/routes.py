from flask import jsonify, request

from app.agendamentos import agendamentos_blueprint
from db.connect_db import get_db_connection
from model.agendamento import Agendamento
from model.agendamento_dto import AgendamentoDto


@agendamentos_blueprint.route('/agendamentos', methods=['GET'])
def get_agendamentos():
    connection = get_db_connection()

    try:
        with connection.cursor() as cursor:
            cursor.execute("SELECT * FROM agendamentos")
            agendamentos = cursor.fetchall()

        agendamentos_json = [{'id': agendamento[0],
                              'data': agendamento[1],
                              'hora': agendamento[2],
                              'valorTotal': agendamento[3],
                              'tempoDuracaoEmMinutos': agendamento[4],
                              'idBarbearia': agendamento[5],
                              'idUsuario': agendamento[6]} for agendamento in agendamentos]

        return jsonify({"agendamentos": agendamentos_json}), 200
    finally:
        connection.close()


@agendamentos_blueprint.route('/agendamentos/<agendamento_id>', methods=['GET'])
def get_agendamento_by_id(agendamento_id):
    connection = get_db_connection()

    try:
        with connection.cursor() as cursor:
            cursor.execute("SELECT * FROM agendamentos WHERE id = %s", (agendamento_id,))
            agendamento: Agendamento = cursor.fetchone()

        if agendamento:
            agendamento_json = {
                'id': agendamento[0],
                'data': agendamento[1],
                'hora': agendamento[2],
                'valorTotal': agendamento[3],
                'tempoDuracaoEmMinutos': agendamento[4],
                'idBarbearia': agendamento[5],
                'idUsuario': agendamento[6]
            }

            return jsonify(agendamento_json), 200

        return jsonify({"mensagem": "Agendamento não encontrado."}), 404
    except Exception as e:
        return jsonify({'mensagem': f'Erro ao buscar agendamento: {str(e)}'}), 500
    finally:
        connection.close()


@agendamentos_blueprint.route('/agendamentos/barbearia/<barbearia_id>', methods=['GET'])
def get_agendamento_by_barbearia_id(barbearia_id):
    connection = get_db_connection()

    try:
        with connection.cursor() as cursor:
            cursor.execute("SELECT * FROM agendamentos WHERE id_barbearia = %s", (barbearia_id,))
            agendamentos = cursor.fetchall()

        agendamentos_json = [{'id': agendamento[0],
                              'data': agendamento[1],
                              'hora': agendamento[2],
                              'valorTotal': agendamento[3],
                              'tempoDuracaoEmMinutos': agendamento[4],
                              'idBarbearia': agendamento[5],
                              'idUsuario': agendamento[6]} for agendamento in agendamentos]

        return jsonify({"agendamentos": agendamentos_json}), 200
    except Exception as e:
        return jsonify({'mensagem': f'Erro ao buscar agendamentos: {str(e)}'}), 500
    finally:
        connection.close()


@agendamentos_blueprint.route('/agendamentos', methods=['POST'])
def insert_agendamento():
    agendamento_data = request.json
    agendamento_dto = AgendamentoDto(data=agendamento_data['data'],
                                     hora=agendamento_data['hora'],
                                     valor_total=agendamento_data['valorTotal'],
                                     tempo_duracao_em_minutos=agendamento_data['tempoDuracaoEmMinutos'],
                                     id_barbearia=agendamento_data['idBarbearia'],
                                     id_usuario=agendamento_data['idUsuario'])
    agendamento = Agendamento(**agendamento_dto.__dict__)
    servicos = agendamento_data['servicos']
    barbeiros = agendamento_data['barbeiros']

    connection = get_db_connection()

    try:
        with connection.cursor() as cursor:
            cursor.execute("INSERT INTO agendamentos (data, hora, valor_total, tempo_duracao_em_minutos, "
                           "id_barbearia, id_usuario) VALUES (%s, %s, %s, %s, %s, %s) RETURNING id",
                           (agendamento.data, agendamento.hora, agendamento.valor_total,
                            agendamento.tempo_duracao_em_minutos, agendamento.id_barbearia, agendamento.id_usuario))

            id_novo_agendamento = cursor.fetchone()[0]

            for servico in servicos:
                cursor.execute("INSERT INTO agendamento_servicos (id_agendamento, id_servico) VALUES (%s, %s)",
                               (id_novo_agendamento, servico))

            for barbeiro in barbeiros:
                cursor.execute("INSERT INTO agendamento_barbeiros (id_agendamento, id_barbeiro) VALUES (%s, %s)",
                               (id_novo_agendamento, barbeiro))

            connection.commit()

        return jsonify({"mensagem": f"Agendamento inserido com sucesso com id {id_novo_agendamento}!"}), 201
    except Exception as e:
        connection.rollback()
        return jsonify({"mensagem": f"Erro ao inserir Agendamento: {str(e)}"}), 500
    finally:
        connection.close()


@agendamentos_blueprint.route('/agendamentos/<agendamento_id>', methods=['PUT'])
def update_agendamento_by_id(agendamento_id):
    dados_atualizados = request.json
    connection = get_db_connection()

    try:
        with connection.cursor() as cursor:
            cursor.execute("SELECT * FROM agendamentos WHERE id = %s", (agendamento_id,))

            agendamento = cursor.fetchone()
            if not agendamento:
                return jsonify({"mensagem": "Agendamento não encontrado!"}), 404

            campos_validos = ["data", "hora", "valor_total", "tempo_duracao_em_minutos"]
            for campo, valor_atualizado in dados_atualizados.items():
                if campo in campos_validos:
                    cursor.execute(f"UPDATE agendamentos SET {campo} = %s WHERE id = %s",
                                   (valor_atualizado, agendamento_id))

            connection.commit()

        return jsonify({"mensagem": "Agendamento atualizado com sucesso!"}), 200
    except Exception as e:
        return jsonify({"erro": f"Erro ao atualizar Agendamento: {str(e)}"}), 500
    finally:
        connection.close()


@agendamentos_blueprint.route('/agendamentos/<agendamento_id>', methods=['DELETE'])
def delete_agendamento_by_id(agendamento_id):
    connection = get_db_connection()

    try:
        with connection.cursor() as cursor:
            cursor.execute("DELETE FROM agendamento_servicos WHERE id_agendamento = %s", (agendamento_id,))
            cursor.execute("DELETE FROM agendamento_barbeiros WHERE id_agendamento = %s", (agendamento_id,))
            cursor.execute("DELETE FROM agendamentos WHERE id = %s", (agendamento_id,))

            connection.commit()

        return jsonify({}), 204
    finally:
        connection.close()
