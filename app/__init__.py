from flask import Flask
from .barbearias.routes import barbearias_blueprint
from .barbeiros.routes import barbeiros_blueprint


def create_app():
    app = Flask(__name__)
    app.register_blueprint(barbearias_blueprint)
    app.register_blueprint(barbeiros_blueprint)

    return app
