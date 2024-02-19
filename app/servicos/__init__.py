from flask import Blueprint

servicos_blueprint = Blueprint('servicos', __name__)

from . import routes
