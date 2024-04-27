import json
import os
from datetime import datetime, timedelta
from collections import namedtuple
from typing import Dict, Any

import bcrypt
import jwt
from flask import request, jsonify

from app.auth import auth_blueprint
from db.connect_db import get_db_connection
from model.usuario import Usuario
from model.usuario_request_dto import UsuarioRequestDto
from model.barbearia.barbearia_request_dto import BarbeariaRequestDto
from model.barbearia.barbearia import Barbearia

secret_key = os.getenv('SECRET_KEY')


@auth_blueprint.route('/auth/user/login', methods=['POST'])
def login_usuario():
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
                        token_payload = create_token_payload(email)
                        token = jwt.encode(token_payload, secret_key, algorithm='HS256')
                        usuario_json = usuario_to_json(usuario)

                        return jsonify({"access_token": token, "usuario": usuario_json}), 200

                return jsonify({"não autorizado": "Email ou senha incorreta!"}), 401
        finally:
            connection.close()
    else:
        return jsonify({"erro": "Informe email e senha válidos..."}), 401


@auth_blueprint.route('/auth/user/register', methods=['POST'])
def register_user():
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

@auth_blueprint.route('/auth/barbearia/login', methods=['POST'])
def login_barbearia():
    barbearia_login_data = request.json
    email = barbearia_login_data['email']
    senha = barbearia_login_data['senha']

    if email and senha:
        connection = get_db_connection()

        try:
            with connection.cursor() as cursor:
                cursor.execute("SELECT * FROM barbearias WHERE email = %s", (email,))
                barbearia = cursor.fetchone()
                if barbearia:
                    senha_cadastrada_barbearia = barbearia[6]
                    if senha_cadastrada_barbearia and is_valid_password(senha, senha_cadastrada_barbearia):
                        token_payload = create_token_payload(email)
                        token = jwt.encode(token_payload, secret_key, algorithm='HS256')
                        barbearia_json = barbearia_to_json(barbearia)

                        return jsonify({"access_token": token, "barbearia": barbearia_json}), 200

                return jsonify({"não autorizado": "Email ou senha incorreta!"}), 401
        finally:
            connection.close()
    else:
        return jsonify({"erro": "Informe email e senha válidos..."}), 401

@auth_blueprint.route('/auth/barbearia/register', methods=['POST'])
def register_barbearia():
    barbearia_data = request.json
    barbearia_dto = BarbeariaRequestDto(cnpj=barbearia_data['cnpj'],
                                    nome=barbearia_data['nome'],
                                    endereco=barbearia_data['endereco'],
                                    email=barbearia_data['email'],
                                    telefone=barbearia_data['telefone'],
                                    senha=barbearia_data['senha'])
    barbearia = Barbearia(**barbearia_dto.__dict__)
    connection = get_db_connection()

    barbearia.senha = hash_senha(barbearia.senha)

    try:
        with connection.cursor() as cursor:
            cursor.execute("INSERT INTO barbearias (cnpj, nome, endereco, email, telefone, senha) VALUES (%s, "
                           "%s, %s, %s, %s, %s) RETURNING id",
                           (barbearia.cnpj, barbearia.nome, barbearia.endereco, barbearia.email, barbearia.telefone,
                            barbearia.senha))

            id_nova_barbearia = cursor.fetchone()[0]
            connection.commit()

        return jsonify({"mensagem": f"Barbearia inserida com sucesso com id {id_nova_barbearia}!"}), 201
    except Exception as e:
        return jsonify({"mensagem": f"Erro ao inserir Barbearia: {str(e)}"})
    finally:
        connection.close()

def hash_senha(senha):
    hashed_senha = bcrypt.hashpw(senha.encode('utf8'), bcrypt.gensalt())
    return hashed_senha.decode('utf-8')


def is_valid_password(password, hashedpassword):
    return bcrypt.checkpw(password.encode('utf-8'), hashedpassword.encode('utf-8'))


def usuario_to_json(usuario: tuple) -> dict[str, str]:
    return {
        'id': usuario[0],
        'cpf': usuario[1],
        'nome': usuario[2],
        'dataNascimento': usuario[3],
        'email': usuario[4]
    }

def barbearia_to_json(barbearia: tuple) -> dict[str, str]:
    return {
        'id': barbearia[0],
        'cnpj': barbearia[1],
        'nome': barbearia[2],
        'endereco': barbearia[3],
        'email': barbearia[4],
        'telefone': barbearia[5]
    }


def create_token_payload(email: str) -> dict[str, str]:
    return {
        'email': email,
        'exp': datetime.utcnow() + timedelta(days=1)
    }
