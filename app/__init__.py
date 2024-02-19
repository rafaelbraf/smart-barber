from flask import Flask
from .barbearias.routes import barbearias_blueprint
from .barbeiros.routes import barbeiros_blueprint
from .servicos.routes import servicos_blueprint


def create_app():
    app = Flask(__name__)
    app.register_blueprint(barbearias_blueprint)
    app.register_blueprint(barbeiros_blueprint)
    app.register_blueprint(servicos_blueprint)

    return app
