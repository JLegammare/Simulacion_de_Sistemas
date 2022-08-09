from dataclasses import dataclass


@dataclass
class Config:
    M: int
    N: int
    Rc: float
    L: float
    static_file: str
    dynamic_file: str
    output_file: str
