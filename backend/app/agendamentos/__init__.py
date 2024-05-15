from flask import Flask, Blueprint

agendamentos_blueprint = Blueprint('agendamentos', __name__)

from . import routes