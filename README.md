# Two-Factor Authentication Demo with Spring Security

To launch the server locally install Docker and run `./mvnw spring-boot:run`

Visit http://localhost:8080/signup to register for an account.

<img width="1912" alt="image" src="https://github.com/making/blog.ik.am/assets/106908/b93b706e-356b-42b4-b97e-bf0e60ca6885">

2FA is disabled by default.

<img width="1912" alt="image" src="https://github.com/making/blog.ik.am/assets/106908/cce5282f-09b5-484f-b876-5c93d87b33fc">

Log out.

<img width="1912" alt="image" src="https://github.com/making/blog.ik.am/assets/106908/5f511532-f48e-460a-b825-6d7f896e5b3e">

Log in again.

<img width="1912" alt="image" src="https://github.com/making/blog.ik.am/assets/106908/bcd1b6cc-c169-443b-a63a-4fe83dd4add1">

2FA is disabled, so login is successful with only username and password.

<img width="1912" alt="image" src="https://github.com/making/blog.ik.am/assets/106908/fd7773c9-e512-4c3c-b284-5af4ff9858fc">

Enable 2FA.

<img width="1912" alt="image" src="https://github.com/making/blog.ik.am/assets/106908/daea5be5-998c-4e73-a8d1-bba8f10d87f9">

Read the QR code using Google Authenticator.

![image](https://github.com/making/blog.ik.am/assets/106908/a1a51abd-6b93-4210-a004-75a801d04040)

Check the code.

![image](https://github.com/making/blog.ik.am/assets/106908/542691b9-f8d3-4a5a-bd0b-51a711f59fae)

Enter the code and click the verify button.

<img width="1912" alt="image" src="https://github.com/making/blog.ik.am/assets/106908/06b6f1d6-3190-4a62-831e-bd8d3d62e3d8">

2FA has been enabled.

<img width="1912" alt="image" src="https://github.com/making/blog.ik.am/assets/106908/78de0e76-257f-4f6d-9489-3ce27db9668f">

Log out.

<img width="1912" alt="image" src="https://github.com/making/blog.ik.am/assets/106908/5f511532-f48e-460a-b825-6d7f896e5b3e">

Log in again.

<img width="1912" alt="image" src="https://github.com/making/blog.ik.am/assets/106908/bcd1b6cc-c169-443b-a63a-4fe83dd4add1">

This time, 2FA is enabled, so you will be asked to enter a code.

<img width="1912" alt="image" src="https://github.com/making/blog.ik.am/assets/106908/49324a33-e6bd-42cb-8268-0e4e1cad3997">

Check the code with Google Authenticator.

![image](https://github.com/making/blog.ik.am/assets/106908/3b1d7927-5c50-4732-a0dd-c4274934f4be)

Enter the code and click the verify button.

<img width="1912" alt="image" src="https://github.com/making/blog.ik.am/assets/106908/7866fe1c-4245-45cc-9a52-b810361a4cb3">

Login was successful.

<img width="1912" alt="image" src="https://github.com/making/blog.ik.am/assets/106908/01ea35a9-2f2b-48d9-a460-9653e02fac12">
