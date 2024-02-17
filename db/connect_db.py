import psycopg2

db_params = {
    'dbname': 'barbearia-app',
    'user': 'postgres',
    'password': 'minha_senha',
    'host': 'localhost',
    'port': '5432'
}


def get_db_connection():
    return psycopg2.connect(**db_params)
