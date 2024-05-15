from flask import Blueprint

usuarios_blueprint = Blueprint('usuarios', __name__)

from . import routes
