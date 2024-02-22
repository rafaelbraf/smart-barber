from flask import Flask

from .agendamentos.routes import agendamentos_blueprint
from .auth import auth_blueprint
from .barbearias.routes import barbearias_blueprint
from .barbeiros.routes import barbeiros_blueprint
from .servicos.routes import servicos_blueprint
from .usuarios import usuarios_blueprint


def create_app():
    app = Flask(__name__)
    app.register_blueprint(auth_blueprint)
    app.register_blueprint(barbearias_blueprint)
    app.register_blueprint(barbeiros_blueprint)
    app.register_blueprint(servicos_blueprint)
    app.register_blueprint(agendamentos_blueprint)
    app.register_blueprint(usuarios_blueprint)

    return app
