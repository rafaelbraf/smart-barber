from flask import Blueprint

barbeiros_blueprint = Blueprint('/barbeiros', __name__)

from . import routes
