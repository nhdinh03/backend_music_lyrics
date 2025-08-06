from time import sleep

BLUE = '\033[94m'
RED_BOLD = '\033[1;91m'
RESET = '\033[0m'

def print_lyrics():
    lines = [
        ("Your morning eyes", 2.1),
        ("I could stare like watching stars", 3.3),
        ("I could walk you by", 2.8),
        ("and I'll tell without a thought", 3.3),
        ("You'd be mine", 2.0),
        ("Would you mind ", 1.9),
        ("If I took your hand tonight?", 3.6),
        ("Know you're all that I want ", 4.2),
        ("This life", 3.7),
        ("I'll imagine we fell in love", 2.9),
        ("I'll nap under moonlight skies with you", 3.8),
    ]

    sleep(0.4)
    for i, (line, delay) in enumerate(lines):
        color = RED_BOLD if i >= len(lines) - 2 else BLUE
        for char in line:
            print(f"{color}{char}{RESET}", end='', flush=True)
            sleep(delay / len(line))
        print()

if __name__ == "__main__":
    print_lyrics()
