export interface ApiResponse<T> {
  success: boolean;
  mensaje?: string;
  data?: T;
  timestamp: string;
}

export interface PagedResponse<T> {
  content: T[];
  pageable: {
    pageNumber: number;
    pageSize: number;
  };
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
  size: number;
  number: number;
}