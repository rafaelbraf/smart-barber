from flask import jsonify, request

from app.servicos import servicos_blueprint
from db.connect_db import get_db_connection
from model.servico import Servico
from model.servico_dto import ServicoDto


@servicos_blueprint.route('/servicos', methods=['GET'])
def get_servicos():
    connection = get_db_connection()

    try:
        with connection.cursor() as cursor:
            cursor.execute("SELECT * FROM servicos")
            servicos = cursor.fetchall()

        servicos_json = [{'id': servico[0],
                          'nome': servico[1],
                          'preco': servico[2],
                          'tempoDuracaoEmMinutos': servico[3],
                          'idBarbearia': servico[4]} for servico in servicos]

        return jsonify({"servicos": servicos_json}), 200
    finally:
        connection.close()


@servicos_blueprint.route('/servicos/<servico_id>', methods=['GET'])
def get_servico_by_id(servico_id):
    connection = get_db_connection()

    try:
        with connection.cursor() as cursor:
            cursor.execute("SELECT * FROM servicos WHERE id = %s", (servico_id,))
            servico: Servico = cursor.fetchone()

        if servico:
            servico_json = {
                'id': servico[0],
                'nome': servico[1],
                'preco': servico[2],
                'tempoDuracaoEmMinutos': servico[3],
                'idBarbearia': servico[4]
            }

            return jsonify(servico_json), 200

        return jsonify({"mensagem": "Serviço não encontrado."}), 404
    except Exception as e:
        return jsonify({'mensagem': f'Erro ao buscar serviço: {str(e)}'}), 500
    finally:
        connection.close()


@servicos_blueprint.route('/servicos/barbearia/<barbearia_id>', methods=['GET'])
def get_servico_by_barbearia_id(barbearia_id):
    connection = get_db_connection()

    try:
        with connection.cursor() as cursor:
            cursor.execute("SELECT * FROM servicos WHERE id_barbearia = %s", (barbearia_id,))
            servicos = cursor.fetchall()

        if servicos:
            servicos_json = [{'id': servico[0],
                              'nome': servico[1],
                              'preco': servico[2],
                              'tempoDuracaoEmMinutos': servico[3],
                              'idBarbearia': servico[4]} for servico in servicos]

            return jsonify({"servicos": servicos_json}), 200
    except Exception as e:
        return jsonify({'mensagem': f'Erro ao buscar serviços: {str(e)}'}), 500
    finally:
        connection.close()


@servicos_blueprint.route('/servicos', methods=['POST'])
def insert_servico():
    servico_data = request.json
    servico_dto = ServicoDto(nome=servico_data['nome'],
                             preco=servico_data['preco'],
                             tempo_duracao_minutos=servico_data['tempoDuracaoEmMinutos'],
                             id_barbearia=servico_data['idBarbearia'])
    servico = Servico(**servico_dto.__dict__)
    connection = get_db_connection()

    try:
        with connection.cursor() as cursor:
            cursor.execute("INSERT INTO servicos (nome, preco, tempo_duracao_em_minutos, id_barbearia) VALUES (%s, "
                           "%s, %s, %s) RETURNING id",
                           (servico.nome, servico.preco, servico.tempo_duracao_minutos, servico.id_barbearia))

            id_novo_servico = cursor.fetchone()[0]
            connection.commit()

        return jsonify({"mensagem": f"Serviço inserido com sucesso com id {id_novo_servico}!"}), 201
    except Exception as e:
        return jsonify({"mensagem": f"Erro ao inserir Serviço: {str(e)}"})
    finally:
        connection.close()


@servicos_blueprint.route('/servicos/<servico_id>', methods=['PUT'])
def update_servico_by_id(servico_id):
    dados_atualizados = request.json
    connection = get_db_connection()

    try:
        with connection.cursor() as cursor:
            cursor.execute("SELECT * FROM servicos WHERE id = %s", (servico_id,))

            barbearia = cursor.fetchone()
            if not barbearia:
                return jsonify({"mensagem": "Serviço não encontrado!"}), 404

            campos_validos = ["nome", "preco", "tempo_duracao_em_minutos"]
            for campo, valor_atualizado in dados_atualizados.items():
                if campo == "tempoDuracaoEmMinutos":
                    campo = "tempo_duracao_em_minutos"

                if campo in campos_validos:
                    cursor.execute(f"UPDATE servicos SET {campo} = %s WHERE id = %s",
                                   (valor_atualizado, servico_id))

            connection.commit()

        return jsonify({"mensagem": "Serviço atualizado com sucesso!"}), 200
    except Exception as e:
        return jsonify({"erro": f"Erro ao atualizar Serviço: {str(e)}"}), 500
    finally:
        connection.close()


@servicos_blueprint.route('/servicos/<servico_id>', methods=['DELETE'])
def delete_servico_by_id(servico_id):
    connection = get_db_connection()

    try:
        with connection.cursor() as cursor:
            cursor.execute("DELETE FROM servicos WHERE id = %s", (servico_id,))
            connection.commit()

        return jsonify({}), 204
    finally:
        connection.close()
