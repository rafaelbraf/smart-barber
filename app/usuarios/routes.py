from flask import jsonify, request

from app.usuarios import usuarios_blueprint
from db.connect_db import get_db_connection
from model.usuario import Usuario
from model.usuario_request_dto import UsuarioRequestDto
from model.usuario_response_dto import UsuarioResponseDto

import bcrypt


@usuarios_blueprint.route('/usuarios', methods=['GET'])
def get_usuarios():
    connection = get_db_connection()

    try:
        with connection.cursor() as cursor:
            cursor.execute("SELECT * FROM usuarios")
            usuarios = cursor.fetchall()

        usuarios_json = [{'id': usuario[0],
                          'cpf': usuario[1],
                          'nome': usuario[2],
                          'dataNascimento': usuario[3],
                          'email': usuario[4],
                          'celular': usuario[5]} for usuario in usuarios]

        return jsonify({"usuarios": usuarios_json}), 200
    finally:
        connection.close()


@usuarios_blueprint.route('/usuarios/<usuario_id>', methods=['GET'])
def get_usuario_by_id(usuario_id):
    connection = get_db_connection()

    try:
        with connection.cursor() as cursor:
            cursor.execute("SELECT * FROM usuarios WHERE id = %s", (usuario_id,))
            usuario: UsuarioResponseDto = cursor.fetchone()

        if usuario:
            usuario_json = {
                'id': usuario[0],
                'cpf': usuario[1],
                'nome': usuario[2],
                'dataNascimento': usuario[3],
                'email': usuario[4],
                'celular': usuario[5]
            }

            return jsonify(usuario_json), 200

        return jsonify({"mensagem": "Usuário não encontrado."}), 404
    except Exception as e:
        return jsonify({'mensagem': f'Erro ao buscar usuário: {str(e)}'}), 500
    finally:
        connection.close()


@usuarios_blueprint.route('/usuarios', methods=['POST'])
def insert_usuario():
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


@usuarios_blueprint.route('/usuarios/<usuario_id>', methods=['PUT'])
def update_usuario_by_id(usuario_id):
    dados_atualizados = request.json
    connection = get_db_connection()

    try:
        with connection.cursor() as cursor:
            cursor.execute("SELECT * FROM usuarios WHERE id = %s", (usuario_id,))

            usuario = cursor.fetchone()
            if not usuario:
                return jsonify({"mensagem": "Usuário não encontrado!"}), 404

            campos_validos = ["nome", "data_nascimento", "email", "endereco", "celular"]
            for campo, valor_atualizado in dados_atualizados.items():
                if campo in campos_validos:
                    cursor.execute(f"UPDATE usuarios SET {campo} = %s WHERE id = %s",
                                   (valor_atualizado, usuario_id))

            connection.commit()

        return jsonify({"mensagem": "Usuário atualizado com sucesso!"}), 200
    except Exception as e:
        return jsonify({"erro": f"Erro ao atualizar Usuário: {str(e)}"}), 500
    finally:
        connection.close()


@usuarios_blueprint.route('/usuarios/<usuario_id>', methods=['DELETE'])
def delete_usuario_by_id(usuario_id):
    connection = get_db_connection()

    try:
        with connection.cursor() as cursor:
            cursor.execute("DELETE FROM usuarios WHERE id = %s", (usuario_id,))
            connection.commit()

        return jsonify({}), 204
    finally:
        connection.close()


def hash_senha(senha):
    salt = bcrypt.gensalt()
    hashed_senha = bcrypt.hashpw(senha.encode('utf-8'), salt)

    return hashed_senha


def is_senha_valida(senha, senha_hasheada):
    return bcrypt.checkpw(senha.encode('utf-8'), senha_hasheada)
