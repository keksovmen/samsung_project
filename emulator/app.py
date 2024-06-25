from flask import Flask
import data
import random


app = Flask(__name__)

@app.route("/")
def hello_world():
	return {"title" : "negro"}

@app.route("/info")
def box_info():
	data.box.unique_id = random.randint(1, 255)
	return data.box.__dict__

@app.route("/property/<int:id>")
def property_info(id):
	return next(filter(lambda v: v.id == id, data.properties)).__dict__

@app.route("/sensor/<int:id>")
def sensor_info(id):
	return next(filter(lambda v: v.id == id, data.sensors)).__dict__

@app.route("/switch/<int:id>")
def switch_info(id):
	return next(filter(lambda v: v.id == id, data.switches)).__dict__

@app.route("/sensor/<int:id>/<int:timestamp>")
def sensor_data(id, timestamp):
	sensor = next(filter(lambda v: v.id == id, data.sensors))
	return [v.__dict__ for v in data.generate_sensor_data(sensor, timestamp)]

@app.route("/switch/<int:id>/<int:timestamp>")
def switch_data(id, timestamp):
	switch = next(filter(lambda v: v.id == id, data.switches))
	return [v.__dict__ for v in data.generate_switch_data(switch, timestamp)]

@app.route("/property/<int:id>/set/<string:value>", methods=["POST"])
def set_property_value(id, value):
	try:
		data.properties[id].value = value
		return "", 200

	except:
		return "", 40