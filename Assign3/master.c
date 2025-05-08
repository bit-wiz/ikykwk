#include<stdio.h>
#include<mpi.h>
#define arr_size 10

int main(int argc, char *argv[]){
	int rank, size;
	MPI_Init(&argc, &argv);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	MPI_Comm_size(MPI_COMM_WORLD, &size);
	
	if(rank == 0){
		int arr[]= {1,2, 3, 4,5, 6, 8,9,10};
		int global_sum = 0, local_sum = 0, recv_local_sum;
		
		if(arr_size%size == 0){
			int array_element_per_process =     arr_size/size;
			int sub_arr[array_element_per_process];
			for(int i=1; i<size; i++){
				
				for(int j=0; j<array_element_per_process;j++){
					sub_arr[j] = arr[i*array_element_per_process+j];
				}			
				
				MPI_Send(sub_arr, array_element_per_process, MPI_INT, i, 1, MPI_COMM_WORLD);
				MPI_Send(&array_element_per_process, 1, MPI_INT, i, 1, MPI_COMM_WORLD);
			}
			
			for(int j=0; j<array_element_per_process; j++){
				local_sum += arr[j];
			}
			printf("Rank %d: local sum: %d\n", rank, local_sum);
			global_sum += local_sum;
		
		}else{
			int array_element_per_process = arr_size/size + 1;
			int sub_arr[array_element_per_process];
			for(int i=1; i<size; i++){
				if(i == size - 1){
					
					int total_array_size_of_last_process = arr_size - array_element_per_process * i;
					for(int j=0; j< total_array_size_of_last_process; j++){
						sub_arr[j] = arr[i*array_element_per_process+j];
					}
					MPI_Send(&sub_arr, total_array_size_of_last_process, MPI_INT, i, 1, MPI_COMM_WORLD);
					MPI_Send(&total_array_size_of_last_process, 1, MPI_INT, i, 1, MPI_COMM_WORLD);
				}else{
					
					for(int j=0; j<array_element_per_process;j++){
						sub_arr[j] = arr[i*array_element_per_process+j];
					}				
					MPI_Send(&sub_arr, array_element_per_process, MPI_INT, i, 1, MPI_COMM_WORLD);
					MPI_Send(&array_element_per_process, 1, MPI_INT, i, 1, MPI_COMM_WORLD);
				}
			}
			
			for(int j=0; j<array_element_per_process; j++){
				local_sum += arr[j];
			}
			printf("Rank %d: local sum: %d\n", rank, local_sum);
			global_sum += local_sum;
		}
		
		
		for(int i=1; i<size; i++){
			MPI_Recv(&recv_local_sum, 1, MPI_INT, i, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
			global_sum += recv_local_sum;
		}
		
		printf("The sum of the array is %d\n", global_sum);
	
	}else{
		
		int array_element_per_process = arr_size/size + 1;
		int recv_sub_arr[array_element_per_process];
		int recv_array_element_per_process, local_sum = 0;

		MPI_Recv(recv_sub_arr, recv_array_element_per_process, MPI_INT, 0, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
		MPI_Recv(&recv_array_element_per_process, 1, MPI_INT, 0, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
		
		for(int j=0; j<recv_array_element_per_process; j++){
			local_sum += recv_sub_arr[j];
		}
		
		printf("Rank %d: local sum: %d\n", rank, local_sum);
		
		MPI_Send(&local_sum, 1, MPI_INT, 0, 1, MPI_COMM_WORLD);
	}
	MPI_Finalize();
	return 0;
}