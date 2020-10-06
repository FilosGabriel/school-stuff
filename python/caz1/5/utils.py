def process_item(x):
    test = lambda a: a == 1 or a == 2 or a == 3 or a % 2 != 0 and len(
        [i for i in range(3, int(a / 2)) if a % i == 0]) == 0
    i = x + 1 if x % 2 == 0 else x + 2
    while not test(i):
        i += 2
    return i

if __name__ == "__main__":
    x=input("Number: ")
    print(process_item(int(x)))