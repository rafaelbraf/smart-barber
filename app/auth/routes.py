import os
from datetime import datetime, timedelta

import bcrypt
import jwt
from flask import request, jsonify

from app.auth import auth_blueprint
from db.connect_db import get_db_connection
from model.usuario import Usuario
from model.usuario_request_dto import UsuarioRequestDto

secret_key = os.getenv('SECRET_KEY')


@auth_blueprint.route('/auth/login', methods=['POST'])
def login():
    usuario_login_data = request.json
    email = usuario_login_data['email']
    senha = usuario_login_data['senha']

    if email and senha:
        connection = get_db_connection()

        try:
            with connection.cursor() as cursor:
                cursor.execute("SELECT * FROM usuarios WHERE email = %s", (email,))
                usuario = cursor.fetchone()
                if usuario:
                    senha_cadastrada_usuario = usuario[6]
                    if senha_cadastrada_usuario and is_valid_password(senha, senha_cadastrada_usuario):
                        token_payload = {
                            'email': email,
                            'exp': datetime.utcnow() + timedelta(days=1)
                        }
                        token = jwt.encode(token_payload, secret_key, algorithm='HS256')

                        return jsonify({"access_token": token}), 200

                return jsonify({"não autorizado": "Email ou senha incorreta!"}), 401
        finally:
            connection.close()
    else:
        return jsonify({"erro": "Informe email e senha válidos..."}), 401


@auth_blueprint.route('/auth/register', methods=['POST'])
def register():
    usuario_data = request.json
    usuario_dto = UsuarioRequestDto(cpf=usuario_data['cpf'],
                                    nome=usuario_data['nome'],
                                    data_nascimento=usuario_data['dataNascimento'],
                                    email=usuario_data['email'],
                                    celular=usuario_data['celular'],
                                    senha=usuario_data['senha'])
    usuario = Usuario(**usuario_dto.__dict__)
    connection = get_db_connection()

    usuario.senha = hash_senha(usuario.senha)

    try:
        with connection.cursor() as cursor:
            cursor.execute("INSERT INTO usuarios (cpf, nome, data_nascimento, email, celular, senha) VALUES (%s, "
                           "%s, %s, %s, %s, %s) RETURNING id",
                           (usuario.cpf, usuario.nome, usuario.data_nascimento, usuario.email, usuario.celular,
                            usuario.senha))

            id_novo_usuario = cursor.fetchone()[0]
            connection.commit()

        return jsonify({"mensagem": f"Usuário inserido com sucesso com id {id_novo_usuario}!"}), 201
    except Exception as e:
        return jsonify({"mensagem": f"Erro ao inserir Usuário: {str(e)}"})
    finally:
        connection.close()


def hash_senha(senha):
    hashed_senha = bcrypt.hashpw(senha.encode('utf8'), bcrypt.gensalt())
    return hashed_senha.decode('utf-8')


def is_valid_password(password, hashedpassword):
    return bcrypt.checkpw(password.encode('utf-8'), hashedpassword.encode('utf-8'))
