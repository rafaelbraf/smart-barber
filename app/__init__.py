import os

import jwt
from flask import Flask, request, jsonify

from .agendamentos.routes import agendamentos_blueprint
from .auth import auth_blueprint
from .barbearias.routes import barbearias_blueprint
from .barbeiros.routes import barbeiros_blueprint
from .servicos.routes import servicos_blueprint
from .usuarios import usuarios_blueprint


def create_app():
    app = Flask(__name__)
    app.config['SECRET_KEY'] = os.getenv('SECRET_KEY')

    @app.before_request
    def before_request():
        if request.blueprint == 'auth':
            return

        token = request.headers.get("Authorization")
        if token is None:
            return jsonify({"mensagem": "Token inválido!"})

        token = token.split(" ")[1]

        try:
            data = jwt.decode(token, app.config['SECRET_KEY'], algorithms=['HS256'])
            app.user_email = data.get('email')
        except jwt.ExpiredSignatureError:
            return jsonify({"mensagem": "Token expirado!"}), 401
        except jwt.InvalidTokenError:
            return jsonify({"mensagem": "Token inválido!"}), 401

    app.register_blueprint(auth_blueprint)
    app.register_blueprint(barbearias_blueprint)
    app.register_blueprint(barbeiros_blueprint)
    app.register_blueprint(servicos_blueprint)
    app.register_blueprint(agendamentos_blueprint)
    app.register_blueprint(usuarios_blueprint)

    return app
