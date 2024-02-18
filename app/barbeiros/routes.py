from flask import jsonify

from app.barbeiros import barbeiros_blueprint
from db.connect_db import get_db_connection
from model.barbeiro import Barbeiro


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

        return jsonify({'barbeiros': barbeiros_json}), 200

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

        return jsonify({"mensagem": "Barbeiro não encontrado"}), 404
    except Exception as e:
        return jsonify({"mensagem": f"Erro ao buscar Barbeiro: {str(e)}"}), 500
    finally:
        connection.close()
