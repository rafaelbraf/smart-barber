from flask import jsonify, request

from app.barbeiros import barbeiros_blueprint
from db.connect_db import get_db_connection
from model.barbeiro.barbeiro import Barbeiro
from model.barbeiro.barbeiro_dto import BarbeiroDTO


@barbeiros_blueprint.route('/barbeiros', methods=['GET'])
def get_barbeiros():
    connection = get_db_connection()

    try:
        with connection.cursor() as cursor:
            cursor.execute("SELECT * FROM barbeiros")
            barbeiros = cursor.fetchall()

        barbeiros_json = [{'id': barbeiro[0],
                           'cpf': barbeiro[1],
                           'nome': barbeiro[2],
                           'email': barbeiro[3],
                           'celular': barbeiro[4],
                           'admin': barbeiro[5],
                           'idBarbearia': barbeiro[6]} for barbeiro in barbeiros]

        response = jsonify({"barbeiros": barbeiros_json})
        response.headers.add('Access-Control-Allow-Origin', '*')

        return response, 200
    finally:
        connection.close()


@barbeiros_blueprint.route('/barbeiros/<barbeiro_id>', methods=['GET'])
def get_barbeiro_por_id(barbeiro_id):
    connection = get_db_connection()

    try:
        with connection.cursor() as cursor:
            cursor.execute("SELECT * FROM barbeiros WHERE id = %s", (barbeiro_id,))
            barbeiro: Barbeiro = cursor.fetchone()

        if barbeiro:
            barbeiro_json = {
                'id': barbeiro[0],
                'cpf': barbeiro[1],
                'nome': barbeiro[2],
                'email': barbeiro[3],
                'celular': barbeiro[4],
                'admin': barbeiro[5],
                'idBarbearia': barbeiro[6]
            }

            return jsonify({"barbeiro": barbeiro_json}), 200

        return jsonify({"mensagem": "Barbeiro não encontrado."}), 404
    except Exception as e:
        return jsonify({"mensagem": f"Erro ao buscar Barbeiro: {str(e)}"}), 500
    finally:
        connection.close()


@barbeiros_blueprint.route('/barbeiros/barbearia/<barbearia_id>', methods=['GET'])
def get_barbeiros_by_barbearia_id(barbearia_id):
    connection = get_db_connection()

    try:
        with connection.cursor() as cursor:
            cursor.execute("SELECT * FROM barbeiros WHERE id_barbearia = %s", (barbearia_id,))
            barbeiros = cursor.fetchall()

        barbeiros_json = [{'id': barbeiro[0],
                           'cpf': barbeiro[1],
                           'nome': barbeiro[2],
                           'email': barbeiro[3],
                           'celular': barbeiro[4],
                           'admin': barbeiro[5],
                           'idBarbearia': barbeiro[6],
                           'ativo': barbeiro[7]} for barbeiro in barbeiros]

        return jsonify({'barbeiros': barbeiros_json}), 200
    finally:
        connection.close()


@barbeiros_blueprint.route('/barbeiros', methods=['POST'])
def insert_barbeiro():
    barbeiro_data = request.json
    barbeiro_dto = BarbeiroDTO(cpf=barbeiro_data['cpf'],
                               nome=barbeiro_data['nome'],
                               email=barbeiro_data['email'],
                               celular=barbeiro_data['celular'],
                               admin=barbeiro_data['admin'],
                               id_barbearia=barbeiro_data['idBarbearia'],
                               ativo=barbeiro_data['ativo'])
    
    if not is_cpf_valido(barbeiro_dto.cpf):
        return jsonify({"mensagem": "Erro ao cadastrar Barbeiro: Informe um CPF válido."}), 400
    
    if not is_celular_valido(barbeiro_dto.celular):
        return jsonify({"mensagem": "Erro ao cadastrar Barbeiro: Informe um Celular válido."}), 400

    barbeiro = Barbeiro(**barbeiro_dto.__dict__)
    connection = get_db_connection()

    try:
        with connection.cursor() as cursor:
            cursor.execute("INSERT INTO barbeiros (cpf, nome, email, celular, admin, id_barbearia) VALUES (%s, %s, "
                           "%s, %s, %s, %s) RETURNING id",
                           (barbeiro.cpf, barbeiro.nome, barbeiro.email, barbeiro.celular, barbeiro.admin,
                            barbeiro.id_barbearia))

            id_novo_barbeiro = cursor.fetchone()[0]
            connection.commit()

        return jsonify({"mensagem": f"Barbeiro criado com sucesso com id {id_novo_barbeiro}!"}), 201
    except Exception as e:
        return jsonify({"mensagem": f"Erro ao criar Barbeiro: {str(e)}"}), 400
    finally:
        connection.close()


@barbeiros_blueprint.route('/barbeiros/<barbeiro_id>', methods=['PUT'])
def update_barbeiro_by_id(barbeiro_id):
    dados_atualizados = request.json
    connection = get_db_connection()

    try:
        with connection.cursor() as cursor:
            cursor.execute("SELECT * FROM barbeiros WHERE id = %s", (barbeiro_id,))

            barbeiro = cursor.fetchone()
            if not barbeiro:
                return jsonify({"mensagem": "Barbeiro não encontrado!"}), 404

            campos_validos = ["cpf", "nome", "email", "celular", "admin"]
            for campo, valor_atualizado in dados_atualizados.items():
                if campo in campos_validos:
                    cursor.execute(f"UPDATE barbeiros SET {campo} = %s WHERE id = %s",
                                   (valor_atualizado, barbeiro_id))

            connection.commit()

        return jsonify({"mensagem": "Barbeiro atualizado com sucesso!"}), 200
    except Exception as e:
        return jsonify({"erro": f"Erro ao atualizar Barbeiro: {str(e)}"}), 500
    finally:
        connection.close()


@barbeiros_blueprint.route('/barbeiros/<barbeiro_id>', methods=['DELETE'])
def delete_barbeiro_by_id(barbeiro_id):
    connection = get_db_connection()

    try:
        with connection.cursor() as cursor:
            cursor.execute("DELETE FROM agendamento_barbeiros WHERE id_barbeiro = %s", (barbeiro_id))
            cursor.execute("DELETE FROM barbeiros WHERE id = %s", (barbeiro_id,))
            connection.commit()

        return jsonify({}), 204
    finally:
        connection.close()

def is_cpf_valido(cpf: str) -> bool:
    return len(cpf) == 11

def is_celular_valido(celular: str) -> bool:
    return len(celular) == 9