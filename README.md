# Booking portal exercise

### Goal:
Develop a client app that will communicate with the given ["Booking Portal" App](server/README.md).

### Description:

The "Booking portal" application purpose is to record/create students payment bookings.
It includes a UI payment form with the following structure.

When the form is submitted the application creates a payment record with the provided information.


![alt tag](https://user-images.githubusercontent.com/34654846/37901679-4a71cd5a-30f2-11e8-83f2-d18ec3f594aa.png)


There are 2 API endpoints which described in the app [Readme file](server/README.md).

### Exercise:
Please develop a second application, inside the client directory. The second application will communicate with the "booking portal" app (- the Server)  in order to accomplish the following:

Implement an API endpoint:  /payments_with_quality_check.  which returns a JSON response    with the next structure:

```json
{
  "bookings_with_quality_check": [
    {
      "reference": string,
      "amount": number,
      "amountWithFees": number,
      "amountReceived": number,
      "qualityCheck": string separated by commas,
      "overPayment": boolean, 
      "underPayment": boolean
    }
  ]
}
```
### API Response Fields Details:

#### amountWithFees: number
Fee logic:

|                      Amount | Fee | 
|----------------------------|:---:| 
|                 <= 1000 USD | 5%  |
| > 1000 USD AND <= 10000 USD | 3%  |
|                 > 10000 USD | 2%  |


--
#### qualityCheck: string
Quality check messages to return:

| Quality Check                                                              |      Message      | 
|--------------------------------------------------------------------|:-----------------:| 
| The payment has an invalid email                                   |   InvalidEmail    |
| The user already has a booked payment in the system                | DuplicatedPayment |
| The amount of the payment in USD including the fees  >  1.000.000$ |  AmountThreshold  |
 --
#### overPayment: boolean
**over-payment** field indicates if the user payed more than the tuition amount including fees (in USD), as introduced in [the booking portal](server/README.md).

#### underPayment: boolean
**under-payment** field indicates if the user payed less than the tuition amount including fees (in USD), as introduced in [the booking portal](server/README.md).

### Currency
All amounts referenced above are in USD but you can receive a different currency from the server which will need to be converted to create the extra values.

### Client Response Example:


![alt tag](https://user-images.githubusercontent.com/34654846/37902217-fe20f97e-30f3-11e8-9594-fe4d611344b0.png)

### Notes

* The client code should be implemented in a BE programming language of your preference.
* You should not use a FrontEnd library such as Angular/Vue/React or even pure JS for the client part (node.js is allowed).
You may use FrontEnd libraries if you decided to add UI in *addition* to the BE part, but this is not mandatory.
* Tests are required.
* "Booking portal" is a given server application its code should not be change. It should be treated as an 3rd party API.
* Nothing is bulletproof. Hence please don't assume things like - "this server will not have any error"

### Important Note:
The client should be used as the BE of a web system, and the Server is the 3rd-party Payments API which we cannot modify as part the exercise.
For your convenience we added a UI for the DB used by the server so you could add mock payments to the payment repository for testing purposes.
