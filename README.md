This git repository belongs to PT. Hyundai AutoEver Indonesia. Please keep it confidential, do not publish or share to anyone without permission from PT. Hyundai AutoEver Indonesia.

This git repository is for candidates whose profiles are considered as interesting candidates and high-end developers' quality. Our aim is to create a development based in Jakarta with a very high quality of development standard.

# Java Project: Hyundai Test Drive Booking System

## A. Story

* Hyundai car dealer wants to streamline its test drive booking process.
* They need a simple web application that allows them to easily book test drives for their customers' desired car models.

## B. Core Features

### Car Model Management

* Allow the dealership to add, edit, and remove car models available for test drives.
* Store basic information about each car model (brand, model, year, image).

### Test Drive Booking

* Allow the dealership to select a car model, preferred date, and time slot for the test drive for the customer.
* Display available time slots based on the selected car model and date.
* Capture customer information (name, email, phone number).
* Validate input to ensure all required fields are filled and the email format is correct.
* Store booking information in the database.

## C. Technical Requirements

### Backend

* Use Java Programming Language (minimum version Java 11)
* Use Spring Boot to build the backend REST API.
* Use JPA for database interaction (you can use an in-memory database like H2 for simplicity).
* Implement basic input validation using Spring Boot Validation.
* Write unit tests for core functionalities (e.g., booking creation, validation).
* Using Git for source code versioning
* Authentication & Authorization process is not required for this stage

### Frontend (Optional)

* create a simple HTML/CSS/JavaScript/TypeScript/ReactJS frontend to interact with the API.

## D. Evaluation Criteria

### Functionality

* Correct implementation of all core features.
* Proper handling of edge cases (e.g., no available time slots, invalid input).
* Clear and meaningful error messages.

### Code Quality
* Proper design of REST API based on best practices
* Clean, well-structured, and maintainable code.
* Proper use of Spring Boot features and best practices.
* Meaningful variable and method names.
* Appropriate comments explaining the code.

### Testing

* Adequate unit test coverage for core functionalities.

### Documentation

* Well-structured API documentation including URL, method, parameters, payload, & response (e.g., using Swagger)
* Well-structured project guideline including how to set up, build, and use the system.
* Code needs to be regularly committed in Git with appropriate commit message explaining the code changes

## E. Submission

* Please submit your completed work by creating a pull request to the main branch of our repository. Here are the steps to follow:
    1. **Commit your changes**: Ensure all your changes are committed to your local branch (ex: branch named `develop`).
    2. **Include setup instructions**: Add a file named `SETUP.md` with setup instructions and any other relevant information that will help us understand and run your code.
    3. **Push your branch**: Push your branch to the remote repository. And make sure it's only **single** branch.
    4. **Create a pull request**: Navigate to the repository on GitHub and create **single** pull request to the `main` branch.
    5. **Add reviewers**: Please add [rais-haeid](https://github.com/rais-haeid) as a reviewer to the pull request.
    6. **Wait for review**: Please do not merge or push to the main branch directly. Instead, submit a pull request and wait for our review.
* Feel free to submit your work any time, before the deadline.

## F. Tips for Candidates

* Plan the work and break it down into smaller tasks.
* It’s okay to ask for clarifications if you have any doubts about the requirements. You can create a Github Issues for this and then assign to [rais-haeid](https://github.com/rais-haeid).
