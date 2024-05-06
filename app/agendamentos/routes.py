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
            sql_query = """
                SELECT 
                    a.id as agendamento_id, 
                    a.data || 'T' || a.hora || ':00' as data_hora_agendamento_inicio,
                    to_char(to_timestamp(a.data || 'T' || a.hora || ':00', 'YYYY-MM-DD"T"HH24:MI:SS') + interval '1 minute' * a.tempo_duracao_em_minutos, 'YYYY-MM-DD"T"HH24:MI:SS')  as data_hora_agendamento_fim,
                    a.valor_total, 
                    a.tempo_duracao_em_minutos,
                    a.id_barbearia,	
                    u.id as usuario_id,
                    u.nome as usuario_nome,
                    u.email as usuario_email,
                    u.celular as usuario_celular
                FROM agendamentos a 
                INNER JOIN usuarios u ON u.id = a.id_usuario
                WHERE a.id_barbearia = %s;
            """

            cursor.execute(sql_query, (barbearia_id,))
            agendamentos = cursor.fetchall()

        agendamentos_json = [{'id': agendamento[0],
                              'dataHoraAgendamentoInicio': agendamento[1],
                              'dataHoraAgendamentoFim': agendamento[2],
                              'valorTotal': agendamento[3],
                              'tempoDuracaoEmMinutos': agendamento[4],
                              'idBarbearia': agendamento[5],
                              'usuario': {
                                  'id': agendamento[6],
                                  'nome': agendamento[7],
                                  'email': agendamento[8],
                                  'celular': agendamento[9]
                              }} for agendamento in agendamentos]

        return jsonify({"agendamentos": agendamentos_json}), 200
    except Exception as e:
        return jsonify({'mensagem': f'Erro ao buscar agendamentos: {str(e)}'}), 500
    finally:
        connection.close()

@agendamentos_blueprint.route('/agendamentos/barbearia/<barbearia_id>/quantidade', methods=['GET'])
def get_quantidade_agendamentos_by_data_agendamento(barbearia_id):
    data_agendamentos = request.args.get('data_agendamentos')
    if not data_agendamentos:
        return jsonify({'error': 'O parâmetro data_agendamento é obrigatório.'}), 400

    connection = get_db_connection()

    try:
        with connection.cursor() as cursor:
            cursor.execute("SELECT count(*) FROM agendamentos WHERE data = %s and id_barbearia = %s", (data_agendamentos, barbearia_id, ))
            quantidade_agendamentos = cursor.fetchone()

        resultado_json = { 'quantidadeAgendamentos': quantidade_agendamentos[0] }

        return jsonify(resultado_json), 200
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
