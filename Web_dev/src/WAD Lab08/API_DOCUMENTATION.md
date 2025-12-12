# Customer API Documentation

## Base URL
`http://localhost:8080/api/customers`

## Endpoints

### 1. Get All Customers
**GET** `/api/customers`

**Response:** 200 OK
```json
{
  "totalItems": 5,
  "totalPages": 1,
  "customers": [
    {
      "id": 1,
      "customerCode": "C001",
      "fullName": "John Partially Updated",
      "email": "john.updated@example.com",
      "phone": "090123759293",
      "address": "New Address",
      "status": "ACTIVE",
      "createdAt": "2025-12-06T16:10:26"
    },
    {
      "id": 2,
      "customerCode": "C002",
      "fullName": "Jane Smith",
      "email": "jane.smith@example.com",
      "phone": "+1-555-0102",
      "address": "456 Oak Ave, Los Angeles, CA 90001",
      "status": "ACTIVE",
      "createdAt": "2025-12-06T16:10:26"
    },
    {
      "id": 3,
      "customerCode": "C003",
      "fullName": "Bob Johnson",
      "email": "bob.johnson@example.com",
      "phone": "+1-555-0103",
      "address": "789 Pine Rd, Chicago, IL 60601",
      "status": "ACTIVE",
      "createdAt": "2025-12-06T16:10:26"
    },
    {
      "id": 4,
      "customerCode": "C004",
      "fullName": "Alice Brown",
      "email": "alice.brown@example.com",
      "phone": "+1-555-0104",
      "address": "321 Elm St, Houston, TX 77001",
      "status": "INACTIVE",
      "createdAt": "2025-12-06T16:10:26"
    },
    {
      "id": 5,
      "customerCode": "C005",
      "fullName": "Charlie Wilson",
      "email": "charlie.wilson@example.com",
      "phone": "+1-555-0105",
      "address": "654 Maple Dr, Phoenix, AZ 85001",
      "status": "ACTIVE",
      "createdAt": "2025-12-06T16:10:26"
    }
  ],
  "currentPage": 0
}

**GET** `/api/customers/{id}`

**Response:** 200 OK
```json
{
  "id": 1,
  "customerCode": "C001",
  "fullName": "John Partially Updated",
  "email": "john.updated@example.com",
  "phone": "090123759293",
  "address": "New Address",
  "status": "ACTIVE",
  "createdAt": "2025-12-06T16:10:26"
}

**Response:** 404 Not Found
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Customer not found with id: 78",
  "path": "/api/customers/78",
  "details": null,
  "timestamp": "2025-12-12T21:54:55.859192"
}

**GET** `/api/customers/advanced-search?name=&email=&status=`
**Response:** 200 OK
```json
[
  {
    "id": 1,
    "customerCode": "C001",
    "fullName": "John Partially Updated",
    "email": "john.updated@example.com",
    "phone": "090123759293",
    "address": "New Address",
    "status": "ACTIVE",
    "createdAt": "2025-12-06T16:10:26"
  }
]

**POST** `/api/customers/`
**REQUEST BODY**
```json
{
    "customerCode": "C006",
    "fullName": "David Miller",
    "email": "david.miller@example.com",
    "phone": "12345678998",
    "address": "999 Broadway, Seattle, WA 98101"
}
**Response:** 201 Created
```json
{
    "customerCode": "C006",
    "fullName": "David Miller",
    "email": "david.miller@example.com",
    "phone": "12345678998",
    "address": "999 Broadway, Seattle, WA 98101"
}

**PUT** `/api/customers/{id}`
**REQUEST BODY**
```json
{
  "id": 1,
  "customerCode": "C001",
  "fullName": "John Updated",
  "email": "john.updated@example.com",
  "phone": "12325412347",
  "address": "New Address",
  "status": "ACTIVE",
  "createdAt": "2025-12-06T16:10:26"
}
**Response:** 200 OK
```json
{
  "id": 1,
  "customerCode": "C001",
  "fullName": "John Updated",
  "email": "john.updated@example.com",
  "phone": "12325412347",
  "address": "New Address",
  "status": "ACTIVE",
  "createdAt": "2025-12-06T16:10:26"
}

**PATCH** `/api/customers/{id}`
**REQUEST BODY**
```json
{
    "fullName": "John Partially Updated"
}
**Response:** 200 OK
```json
{
  "id": 1,
  "customerCode": "C001",
  "fullName": "John Partially Updated",
  "email": "john.updated@example.com",
  "phone": "12325412347",
  "address": "New Address",
  "status": "ACTIVE",
  "createdAt": "2025-12-06T16:10:26"
}

**DELETE** `/api/customers/{id}`

**Response:** 200 OK
```json
{
  "message": "Customer deleted successfully"
}

**Error Response:** 404 Not Found
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Customer not found with id: 5",
  "path": "/api/customers/5",
  "details": null,
  "timestamp": "2025-12-12T22:05:34.2016754"
}