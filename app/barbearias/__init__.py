from flask import Blueprint

barbearias_blueprint = Blueprint("barbearias", __name__)

from . import routes
