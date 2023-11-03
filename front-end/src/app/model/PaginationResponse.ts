export class PaginationResponse<T> {
    data: T[];
    currentPage: number;
    totalItems: number;
    totalPages: number;
  }