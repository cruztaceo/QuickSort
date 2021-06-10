# QuickSort

```
algorithm quicksort(A, lo, hi) is
    if lo < hi then
        p:= partition(A, lo, hi)
        quicksort(A, lo, p - 1)
        quicksort(A, p + 1, hi) 
```

```
algorithm partition(A, lo, hi) is
    pivot := rand(A)
    i := lo
    for j := lo to hi do
        if A[j] < pivot then
            swap A[i] with A[j]
            i := i + 1
    swap A[i] with A[h]
    return i
```