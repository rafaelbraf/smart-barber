from flask import Flask


def create_app():
    app = Flask(__name__)

    from .barbearias.routes import barbearias_blueprint
    app.register_blueprint(barbearias_blueprint)

    return app
