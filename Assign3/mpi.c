#include <stdio.h>
#include <stdlib.h>
#include <mpi.h>

int main(int argc, char *argv[]) {
    int rank, size;
    MPI_Init(&argc, &argv);                      
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);        
    MPI_Comm_size(MPI_COMM_WORLD, &size);        

    int n, *arr = NULL;

    if (rank == 0) {
        printf("Enter size of array: ");
        scanf("%d", &n);
        arr = malloc(n * sizeof(int));
        printf("Enter %d numbers:\n", n);
        for (int i = 0; i < n; i++) {
            scanf("%d", &arr[i]);
        }
    }

    MPI_Bcast(&n, 1, MPI_INT, 0, MPI_COMM_WORLD);

    // Calculate sendcounts and displacements
    int *sendcounts = malloc(size * sizeof(int));
    int *displs = malloc(size * sizeof(int));
    int base = n / size, extra = n % size;
    for (int i = 0; i < size; i++) {
        sendcounts[i] = base + (i < extra ? 1 : 0);
        displs[i] = (i == 0) ? 0 : displs[i - 1] + sendcounts[i - 1];
    }

    int local_n = sendcounts[rank];
    int *local_arr = malloc(local_n * sizeof(int));

    MPI_Scatterv(arr, sendcounts, displs, MPI_INT,
                 local_arr, local_n, MPI_INT, 0, MPI_COMM_WORLD);

    int local_sum = 0;
    for (int i = 0; i < local_n; i++)
        local_sum += local_arr[i];

    int global_sum = 0;
    MPI_Reduce(&local_sum, &global_sum, 1, MPI_INT, MPI_SUM, 0, MPI_COMM_WORLD);

    if (rank == 0)
        printf("Total sum = %d\n", global_sum);

    if (rank == 0) free(arr);
    free(local_arr);
    free(sendcounts);
    free(displs);

    MPI_Finalize();
    return 0;
}
