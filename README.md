# AsyncRestApi
Example of how to convert a synchronous blocking REST endpoint into a non-blocking endpoint

## POST 
Request to start the job execution asynchronously and returns immediately with a job started response

### Request
```
POST Request -> http://localhost:8080/threadjobs/async
Request Body -> {"jobId":5}
```

### Response
#### If job received successfully
```
Response Status -> 200
Response Message -> Job started for id :: 5
```
#### If job with same id is already in progress
```
Response Status -> 200
Response Message -> Already Processing job with id: 5
```

## GET
Returns the job status unless it has finished in which case it returns the result

### Request
```
GET Request -> http://localhost:8080/threadjobs/5
```

### Response 
#### If job is still in progress
```
Response Status -> 200
Response Message -> Started
```
#### If job has completed
```
Response Status -> 200
Response Message -> Job with id: 5 finished with result = 10
```
#### If no job was found
```
Response Status -> 200
Response Message -> Job with id: 5 not found
```

