class Property:
	def __init__(self, name, description, value_type, min_value, max_value, tid, id, value) -> None:
		self.name = name
		self.description = description
		self.value_type = value_type
		self.min_value = min_value
		self.max_value = max_value
		self.tid = tid
		self.id = id
		self.value = value