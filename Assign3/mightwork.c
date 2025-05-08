#include <stdio.h>
#include <stdlib.h>
#include <mpi.h>
#define OMPI_MCA true

int main(int argc, char *argv[]){
    int rank, size;
    MPI_Init(&argc, &argv);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    int arr_size;        
    int local_sum = 0, global_sum = 0;
    int *arr = NULL;

    
    if(rank == 0){
        printf("Enter length of array: ");
        fflush(stdout);
        scanf("%d", &arr_size);
    }
    
    
    MPI_Bcast(&arr_size, 1, MPI_INT, 0, MPI_COMM_WORLD);

    
    if(rank == 0){
        arr = (int*)malloc(arr_size * sizeof(int));
        for(int i = 0; i < arr_size; i++){
            printf("Enter element %d: ", i+1);
            fflush(stdout);
            scanf("%d", &arr[i]);
        }
    }

    int *sendcounts = (int*)malloc(size * sizeof(int));
    int *displs = (int*)malloc(size * sizeof(int));

    int base_count = arr_size / size;
    int remainder = arr_size % size;
    for(int i = 0; i < size; i++){
        
        sendcounts[i] = base_count + (i < remainder ? 1 : 0);
    }
    displs[0] = 0;
    for(int i = 1; i < size; i++){
        displs[i] = displs[i-1] + sendcounts[i-1];
    }

    
    int local_count = sendcounts[rank];
    int *sub_arr = (int*)malloc(local_count * sizeof(int));

    
    MPI_Scatterv(arr, sendcounts, displs, MPI_INT,
                 sub_arr, local_count, MPI_INT, 0, MPI_COMM_WORLD);

    
    for(int i = 0; i < local_count; i++){
        local_sum += sub_arr[i];
    }
    printf("Rank %d: local sum: %d\n", rank, local_sum);

    
    MPI_Reduce(&local_sum, &global_sum, 1, MPI_INT, MPI_SUM, 0, MPI_COMM_WORLD);

    if(rank == 0){
        printf("The sum of the array is %d\n", global_sum);
        free(arr);
    }

    free(sub_arr);
    free(sendcounts);
    free(displs);
    
    MPI_Finalize();
    return 0;
}

