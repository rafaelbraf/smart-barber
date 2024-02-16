from flask import Flask, jsonify, request
import psycopg2

app = Flask(__name__)

db_params = {
    'dbname': 'barbearia-app',
    'user': 'postgres',
    'password': 'minha_senha',
    'host': 'localhost',
    'port': '5432'
}


def get_db_connection():
    return psycopg2.connect(**db_params)


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
