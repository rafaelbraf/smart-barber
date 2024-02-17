from flask import Flask, jsonify, request

from db.connect_db import get_db_connection
from model.barbearia import Barbearia

app = Flask(__name__)


@app.route('/')
def hello_world():
    return "<p>Hello World!</p>"


@app.route('/barbearias', methods=['GET'])
def get_barbearias():
    connection = get_db_connection()

    try:
        with connection.cursor() as cursor:
            cursor.execute("SELECT * FROM barbearias")
            barbearias = cursor.fetchall()

        barbearias_json = [{'id': barbearia[0],
                            'cnpj': barbearia[1],
                            'nome': barbearia[2],
                            'endereco': barbearia[3],
                            'email': barbearia[4],
                            'telefone': barbearia[5]} for barbearia in barbearias]

        return jsonify({'barbearias': barbearias_json})

    finally:
        connection.close()


@app.route('/barbearias/<barbearia_id>', methods=['GET'])
def get_barbearia_by_id(barbearia_id):
    connection = get_db_connection()

    try:
        with connection.cursor() as cursor:
            cursor.execute("SELECT * FROM barbearias WHERE id = %s", (barbearia_id,))
            barbearia: Barbearia = cursor.fetchone()

        if barbearia:
            barbearia_json = {
                'id': barbearia[0],
                'cnpj': barbearia[1],
                'nome': barbearia[2],
                'endereco': barbearia[3],
                'email': barbearia[4],
                'telefone': barbearia[5]
            }

            return jsonify(barbearia_json)

        return jsonify({"mensagem": "Barbearia não encontrada."})
    except Exception as e:
        return jsonify({'mensagem': f'Erro ao buscar Barbearia: {str(e)}'}), 500
    finally:
        connection.close()


@app.route('/barbearias', methods=['POST'])
def insert_barbearia():
    barbearia: Barbearia = request.json
    connection = get_db_connection()

    try:
        with connection.cursor() as cursor:
            cursor.execute("INSERT INTO barbearias (cnpj, nome, endereco, email, telefone, senha) VALUES (%s, %s, "
                           "%s, %s, %s, %s) RETURNING id",
                           (barbearia['cnpj'], barbearia['nome'], barbearia['endereco'], barbearia['email'],
                            barbearia['telefone'], barbearia['senha']))

            id_nova_barbearia = cursor.fetchone()[0]
            connection.commit()

        return jsonify({'mensagem': f'Barbearia criada com id {id_nova_barbearia}'}), 201
    except Exception as e:
        return jsonify({'mensagem': f'Erro ao criar Barbearia: {str(e)}'}), 500
    finally:
        connection.close()


@app.route('/barbearias/<barbearia_id>', methods=['PUT'])
def update_barbearia_by_id(barbearia_id):
    dados_atualizados = request.json
    connection = get_db_connection()

    try:
        with connection.cursor() as cursor:
            cursor.execute("SELECT * FROM barbearias WHERE id = %s", (barbearia_id,))

            barbearia = cursor.fetchone()
            if not barbearia:
                return jsonify({"mensagem": "Barbearia não encontrada"})

            campos_validos = ["cnpj", "nome", "endereco", "email", "telefone"]
            for campo, valor_atualizado in dados_atualizados.items():
                if campo in campos_validos:
                    cursor.execute(f"UPDATE barbearias SET {campo} = %s WHERE id = %s",
                                   (valor_atualizado, barbearia_id))

            connection.commit()

        return jsonify({"mensagem": "Barbearia atualizada com sucesso!"}), 200
    except Exception as e:
        return jsonify({"erro": f"Erro ao atualizar Barbearia: {str(e)}"}), 500
    finally:
        connection.close()


@app.route('/barbearias/<barbearia_id>', methods=['DELETE'])
def delete_barbearia_by_id(barbearia_id):
    connection = get_db_connection()

    try:
        with connection.cursor() as cursor:
            cursor.execute("DELETE FROM barbearias WHERE id = %s", (barbearia_id,))
            connection.commit()

        return jsonify({}), 204
    finally:
        connection.close()


if __name__ == '__main__':
    app.run(debug=True)
