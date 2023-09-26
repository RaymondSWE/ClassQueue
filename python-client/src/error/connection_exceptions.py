class ConnectionError(Exception):
    def __init__(self, message="Connection Error"):
        self.message = message
        super().__init__(self.message)

class DeserializationError(Exception):
    def __init__(self, message="Deserialization Error"):
        self.message = message
        super().__init__(self.message)

class EmptyResponseError(Exception):
    def __init__(self, message="Empty response received"):
        self.message = message
        super().__init__(self.message)

class InvalidResponseError(Exception):
    def __init__(self, message="Invalid response format"):
        self.message = message
        super().__init__(self.message)

class SendMessageError(Exception):
    def __init__(self, message="Error sending message"):
        self.message = message
        super().__init__(self.message)
class ServerError(Exception):
    def __init__(self, message):
        super().__init__(message)