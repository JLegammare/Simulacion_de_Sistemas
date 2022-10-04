from dataclasses import dataclass


@dataclass
class Config:
    N: int
    L: float
    static_file: str
    dynamic_file: str
