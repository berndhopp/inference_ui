you need a local triton server in order to run this project
```bash
docker run --rm -p 8000:8000 -p 8001:8001 -p 8002:8002 -v ${PWD}/model_repository:/models nvcr.io/nvidia/tritonserver:25.08-py3 tritonserver --model-repository=/models
``