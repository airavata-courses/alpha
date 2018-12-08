FROM python:3

WORKDIR /app

COPY ./requirements.txt /app
COPY ./app.py /app


RUN pip install -r requirements.txt

ENTRYPOINT ["python", "/app/app.py"]

