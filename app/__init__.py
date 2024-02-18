from flask import Flask
from .barbearias.routes import barbearias_blueprint


def create_app():
    app = Flask(__name__)
    app.register_blueprint(barbearias_blueprint)

    return app
