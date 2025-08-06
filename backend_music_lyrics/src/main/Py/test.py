import time

BLUE = '\033[94m'
RED_BOLD = '\033[1;91m'
RESET = '\033[0m'

def print_lyrics():
    lines = [
        ("Your morning eyes", 0.07),
        ("I could stare like watching stars", 0.07),
        ("I could walk you by", 0.08),
        ("and I'll tell without a thought", 0.08),
        ("You'd be mine", 0.07),
        ("Would you mind if I took your hand tonight?", 0.08),
        ("Know you're all that I want this life", 0.08),
        ("I'll imagine we fell in love", 0.07),
        ("I'll nap under moonlight skies with you", 0.08),
    ]

    time.sleep(0.4)
    for i, (line, delay) in enumerate(lines):
        color = RED_BOLD if i >= len(lines) - 2 else BLUE
        print(f"{color}{line}{RESET}")
        time.sleep(delay)

if __name__ == "__main__":
    print_lyrics()
