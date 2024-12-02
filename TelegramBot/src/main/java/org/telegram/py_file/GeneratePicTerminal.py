import json
import time
import base64
import requests
import argparse

class Text2ImageAPI:
    def __init__(self, url, api_key, secret_key):
        self.URL = url
        self.AUTH_HEADERS = {
            'X-Key': f'Key {api_key}',
            'X-Secret': f'Secret {secret_key}',
        }

    def get_model(self):
        response = requests.get(self.URL + 'key/api/v1/models', headers=self.AUTH_HEADERS)
        data = response.json()
        return data[0]['id']

    def generate(self, prompt, model, images=1, width=1024, height=1024, style=3):
        styles = ["KANDINSKY", "UHD", "ANIME", "DEFAULT"]
        params = {
            "type": "GENERATE",
            "numImages": images,
            "width": width,
            "height": height,
            "style": styles[style],
            "generateParams": {
                "query": f"{prompt}"
            }
        }

        data = {
            'model_id': (None, model),
            'params': (None, json.dumps(params), 'application/json')
        }
        response = requests.post(self.URL + 'key/api/v1/text2image/run', headers=self.AUTH_HEADERS, files=data)
        data = response.json()
        return data['uuid']

    def check_generation(self, request_id, attempts=10, delay=10):
        while attempts > 0:
            response = requests.get(self.URL + 'key/api/v1/text2image/status/' + request_id, headers=self.AUTH_HEADERS)
            data = response.json()
            if data['status'] == 'DONE':
                return data['images']

            attempts -= 1
            time.sleep(delay)

if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='Generate image from text.')
    parser.add_argument('--api_key', required=True, help='API key')
    parser.add_argument('--secret_key', required=True, help='Secret key')
    parser.add_argument('--prompt', required=True, help='Prompt for image generation')
    parser.add_argument('--style', type=int, default=3, help='Style index for image generation')

    args = parser.parse_args()

    api = Text2ImageAPI('https://api-key.fusionbrain.ai/', args.api_key, args.secret_key)
    model_id = api.get_model()
    uuid = api.generate(args.prompt, model_id, style=args.style)
    images = api.check_generation(uuid)

    image_base64 = images[0]
    image_data = base64.b64decode(image_base64)

    with open("image.jpg", "wb") as file:
        file.write(image_data)

    print("Изображение загружено!")