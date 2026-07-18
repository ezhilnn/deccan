from fastapi import FastAPI

app = FastAPI(title="Deccan AI Runtime")

@app.get("/health")
def health():
    return {"status": "UP"}
