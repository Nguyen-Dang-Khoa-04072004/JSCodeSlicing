function foo(a, b) {
    let temp = a
    a = b
    b = temp
    return a + b
}

foo(10, 20)