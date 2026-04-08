---
layout: default.md
title: "Developer Guide"
pageNav: 3
---
# ClientVault Developer Guide

<!-- * Table of Contents -->

<page-nav-print />

---

## **Acknowledgements**

_{ list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well }_

---

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

---

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.

* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
2. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
3. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
4. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:

* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component

**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />

The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>

### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,

* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

---

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Add Property feature

The `addProperty` feature allows users to add a property to a client identified by the index in the displayed client list.

The `AddPropertyCommand` is executed through the following flow:

1. The command retrieves the currently displayed client list using `Model#getFilteredPersonList()`.
2. The target client is identified using the provided index.
3. The command validates whether the property can be added to the target client.
4. A new `Person` object is created with the new property added.
5. The command calls `Model#setPerson(personToEdit, editedPerson)` to update the client.
6. `ModelManager` updates the underlying `AddressBook`.
7. The command returns a `CommandResult`.

For simplicity, the sequence diagram below focuses on the main interactions and omits lower-level validation details.

The following sequence diagram illustrates the interactions:

<puml src="diagrams/AddPropertySequenceDiagram.puml" alt="AddProperty sequence diagram" />

### Edit Client feature

The `editClient` feature allows users to update the details of an existing client identified by an index in the displayed client list.

The `EditClientCommand` is executed through the following flow:

1. The command retrieves the currently displayed client list using `Model#getFilteredPersonList()`.
2. The target client is identified using the provided index.
3. A new `Person` object is created with the updated fields.
4. The command calls `Model#setPerson(personToEdit, editedPerson)` to update the client.
5. `ModelManager` updates the underlying `AddressBook`.
6. The command returns a `CommandResult`.

If one or more `t/` prefixes are provided, the client’s existing tags are replaced. If `t/` is provided without a value, all existing tags are cleared.

For simplicity, the sequence diagram below focuses on the main interactions involved in updating the client and omits lower-level validation details such as index checks and exception handling.

The following sequence diagram illustrates the interactions:

<puml src="diagrams/EditClientSequenceDiagram.puml" alt="EditClient sequence diagram" />

### Edit Property feature

The `editProperty` feature allows users to edit an existing property identified by its index in the displayed property list.

The `EditPropertyCommand` is executed through the following flow:

1. The command retrieves the currently displayed property list using `Model#getFilteredPropertyList()`.
2. The target property is identified using the provided index.
3. The command identifies the client who owns the target property.
4. The property is updated through its owner.
5. A new `Person` object is created with the updated property.
6. The command calls `Model#setPerson(owner, editedPerson)` to update the client.
7. `ModelManager` updates the underlying `AddressBook`.
8. The command returns a `CommandResult`.

Only the specified fields are updated. All other fields remain unchanged.

For simplicity, the sequence diagram below focuses on the main interactions involved in editing a property and omits lower-level validation details such as index checks and exception handling.

The following sequence diagram illustrates the interactions:

<puml src="diagrams/EditPropertySequenceDiagram.puml" alt="EditProperty sequence diagram" />

### Delete Property feature

The delete property feature allows users to delete a property identified by the index in the displayed property list.
This is done by validating the property deletion and updating the target client in the address book.

The `DeletePropertyCommand` is executed through the following flow:
1. The command retrieves the currently displayed property list by calling `Model#getFilteredPropertyList()`.
2. The target property is identified using the provided index.
3. The command validates whether the property can be deleted from the target client.
4. If the property deletion is valid, `DeletePropertyCommand` creates an updated `Person` object without the deleted property.
5. `DeletePropertyCommand` calls `Model#setPerson(personToEdit, editedPerson)`.
6. `ModelManager#setPerson(...)` updates the target client in the underlying `AddressBook`.
7. The command returns a `CommandResult` after the target client has been updated.

For simplicity, the sequence diagram below focuses on the main interactions involved in updating the target client and
omits lower-level validation details such as index checks, ownership checks, and exception handling.

The following sequence diagram illustrates the interactions:

<puml src="diagrams/DeletePropertySequenceDiagram.puml" alt="Interactions between FilterPropertyCommand and ModelManager for filtered list updates" />

### Filter Property feature

The filter property feature allows users to filter properties by address keywords, price range, and size range, and automatically display the owners of those properties. This is done by updating the predicates on the `FilteredList` objects.

The `FilterPropertyCommand` is executed through the following flow:

1. The command is executed with a property predicate (`PropertyMatchesFilterPredicate`) built from the user input, which may include address keywords, price range, and/or size range.
2. `FilterPropertyCommand` calls `Model#updateFilteredPropertyList(predicate)`.
3. `ModelManager#updateFilteredPropertyList(...)` updates the property `FilteredList` by calling `setPredicate(...)`.
4. `FilterPropertyCommand` then calls `Model#updateFilteredPersonList(predicate)`.
5. `ModelManager#updateFilteredPersonList(...)` updates the person `FilteredList` by calling `setPredicate(ownersOfFilteredProperties)`.
6. The command returns a `CommandResult` after both filtered lists have been updated.

The following sequence diagram illustrates the interactions:

<puml src="diagrams/FilterPropertySequenceDiagram.puml" alt="Interactions between FilterPropertyCommand and ModelManager for filtered list updates" />

#### Design Highlights

* **Multi-criteria Filtering**: The `PropertyMatchesFilterPredicate` implements the `Predicate<Property>` interface and supports filtering by address keywords, price range, and size range simultaneously.
* **Address Keyword Matching**: The predicate supports multiple address keywords and performs case-insensitive matching using `StringUtil.containsWordIgnoreCase()`. Keywords use OR logic (properties matching any keyword are included).
* **Numeric Range Filtering**: The predicate supports optional minimum and maximum price and size boundaries. A property must fall within all specified ranges to match.
* **Cascading Filter**: After filtering properties, the command automatically updates the person list to show only those who own matching properties, providing a complete view of relevant data.
* **Flexible Criteria**: At least one filter criterion (address keywords, price range, or size range) must be provided, but users can combine any of these filters as needed.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.

  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.
* **Alternative 2:** Individual command knows how to undo/redo by
  itself.

  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_

---

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

---

## **Appendix: Requirements**

### Product scope

### Target user profile:

* requires way to handle multiple clients and properties concurrently without losing track of key details (e.g., unit size, asking price, buyer requirements) during time-sensitive interactions (calls, viewings, negotiations)
* needs to quickly recall client and property details without navigating through multiple screens or mouse-heavy CRM tools
* tech-savvy property agents
* types fast
* comfortable using CLI apps

### Value Proposition:

ClientVault enables Singapore residential property agents to capture, organize, and retrieve client + property details in seconds through a fast, keyboard-driven interface. This reduces the friction of searching across chat logs and spreadsheets when handling daily tasks such as:

* managing client contacts and roles (buyer/seller)
* recalling unit details (flat type / size / bedrooms / lease remaining)
* tracking buyer requirements (budget, location, preferred property type)
* quickly matching buyers to suitable seller listings.

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`


| Priority | As a …           | I want to …                                                                                              | So that I can…                                                      |
|----------|------------------|----------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------|
| `* * *`  | property agent   | add a client with contact details and role (buyer/seller)                                                | retrieve them quickly during calls                                  |
| `* * *`  | property agent   | view a client’s full profile using a command                                                             | reference key details instantly                                     |
| `* * *`  | property agent   | remove clients who are no longer buying/selling property                                                 | reduce clutter                                                      |
| `* * *`  | property agent   | add a property listing with details (HDB/Condo, location, size, bedrooms, asking price, lease remaining) | store the listings centrally                                        |
| `* * *`  | property agent   | list all active property listings                                                                        | quickly scan what I have available                                  |
| `* * *`  | property agent   | view a property's full details using a command                                                           | reference key details instantly                                     |
| `* * *`  | property agent   | delete property listings that have just been transacted                                                  | reduce clutter                                                      |
| `* *`    | property agent   | find listings that match a buyer’s requirements (budget/type/location)                                   | suggest suitable homes quickly                                      |
| `* *`    | property agent   | update buyer requirements (budget, preferred location, type, min bedrooms)                               | ensure matches remain accurate                                      |
| `* *`    | property agent   | modify any client details without re-adding them                                                         | quickly correct mistakes and keep information updated               |
| `* *`    | property agent   | view a specific property's owner                                                                         | contact the seller quickly when there is a offer for their property |
| `* *`    | property agent   | update listing details (asking price, status, lease remaining)                                           | ensure information stays current                                    |
| `* *`    | property agent   | see a compact summary (type, size, bedrooms, lease, price)                                               | answer quickly without opening multiple fields                      |
| `* *`    | property agent   | use keyboard-friendly commands and aliases                                                               | operate quickly without a mouse                                     |
| `* *`    | property agent   | tag property listings (e.g., “urgent”, “hot lead”, “near MRT”)                                           | input important remarks or features of the property                 |
| `*`      | property agent   | set follow-up reminders                                                                                  | stay on top of deadlines and next steps                             |
| `*`      | property agent   | record seller viewing time windows                                                                       | make scheduling smoother                                            |
| `*`      | property agent   | export a client or listing summary                                                                       | share it with clients or teammates                                  |

*{More to be added}*

## Use Cases

Note: For all use cases, the System is `ClientVault` and the Actor is the `Property Agent`, unless specified otherwise

**Use Case 1: Adding a new client**

Goal: Add a new client's details after first meeting

**MSS:**

1. Actor launches application
2. Actor adds new client with his/her relevant details
3. System adds the new client to the address book
4. Actor reads the system confirmation that the new client has been added

   Use case ends

**Extensions:**

* 2a. System detects error in Actor’s entry format or missing required fields
	* 2a1. System requests for new entry with correct format
	* 2a2. Actor enters data in correct format 
      
      Steps 2a1-2a2 repeats until Actor uses the proper format 

      Use case resumes from step 3

* 2b. System detects duplicate client entry
    * 2b1. System shows an error message that the client already exists in the address book
    * 2b2. Actor enters data with different name
      
      Steps 2b1-2b2 repeats until Actor uses a different name
  
      Use case resumes from step 3

**Use Case 2: Adding a new property**

Goal: Add a new property's details 

**MSS:**

1. Actor launches application
2. Actor adds new property with relevant details
3. System adds the new property to the address book
4. Actor reads the system confirmation that the new property has been added

   Use case ends

**Extensions:**

* 2a. System detects error in Actor’s entry format or missing required fields
    * 2a1. System requests for new entry with correct format
    * 2a2. Actor enters data in correct format

      Steps 2a1-2a2 repeats until Actor uses the proper format

      Use case resumes from step 3

* 2b. System detects duplicate property entry
    * 2b1. System shows an error message that the client already exists in the address book

      Use case ends
  
* 2c. System detects client already has a 'HDB' type property
    * 2c1. System shows an error message that the client already has a 'HDB' type property

      Use case ends

**Use Case 3: View Property Information of a Client**

Goal:  See all properties listed under a specific client

**MSS:**

1. Actor identifies client index in the displayed client list
2. Actor uses the viewClient feature with the client’s index as the parameter
3. System retrieves client at that index
4. System retrieves all properties linked to that client 
5. System displays the list of properties linked to client(indexed)
6. Actor chooses the specific property by index
7. System displays all relevant information about the specific property

   Use Case ends

**Extensions:**

* 2a. Client index is invalid (e.g., out of bounds, not a number)
	* 2a1. System shows error message that client index is invalid

      Step 2a1 repeats until Actor uses a valid client index
       
      Use case resumes from Step 3

* 4a. Client exists but no property listed under client
	* 4a1. System shows message that no property is listed under client
    
      Use case ends

**Use Case 4:  Delete property listing**

Goal: Delete a listing after a successful transaction to reduce clutter in the address book

**MSS:**
1. Actor confirms a property has been sold
2. Actor identifies property index
3. Agent uses the deleteProperty feature with property index
4. System verifies property exists
5. System confirmation that said property is deleted

   Use case ends

**Extension:**
* 3a. Property index is invalid (e.g., out of bounds, not a number)
  * 3a1. System shows error message that property index is invalid
  
    Step 3a1 repeats until Actor uses a valid property index

    Use case resumes from Step 4

**Use Case 5: Delete Client**

Goal: Delete a client and all their associated properties to reduce clutter

**MSS:**

1. Actor identifies client index in the displayed client list
2. Actor uses the deleteClient feature with the client's index
3. System verifies client exists
4. System deletes all properties associated with the client
5. System deletes the client
6. System shows confirmation that the client and their properties have been deleted

   Use case ends

**Extension:**
* 2a. Client index is invalid (e.g., out of bounds, not a number)
    * 2a1. System shows error message that client index is invalid

      Step 2a1 repeats until Actor uses a valid client index

      Use case resumes from Step 3

**Use Case 6: Edit Client**

Goal: Edit a client's details to keep information updated and accurate

**MSS:**

1. Actor identifies client index in the displayed client list
2. Actor uses the editClient feature with the client's index and the fields to update (name, phone, email, address, role, tags)
3. System verifies client exists at the given index
4. System validates the provided field formats
5. System updates the client with the new details
6. System shows confirmation that the client has been successfully updated
   Use case end

**Extension:**
* 2a. Client index is invalid (e.g., out of bounds, not a number)
    * 2a1. System shows error message that client index is invalid

      Step 2a1 repeats until Actor uses a valid client index

      Use case resumes from Step 3

* 2b. System detects no fields are provided to edit
    * 2b1. System shows error message that no fields have been provided to edit

      Step 2b1 repeats until Actor provides at least one field to edit
  
      Use case ends

* 4a. One or more provided field formats are invalid (e.g. invalid phone number, invalid email)
    * 4a1. System shows error message that the provided field format(s) are invalid

      Step 4a1 repeats until Actor provides valid field formats

      Use case resumes from Step 5

* 4b. System detects duplicate client entry after editing (e.g., another client with the same name already exists in the address book)
    * 4b1. System shows error message that the edited client details would result in a duplicate client entry

      Use case ends

<box type="note" seamless>

**Note:**

- Edit Property works the same way, with the following differences:
  - In step 2, the actor uses the property index and the fields to update (address, price, size, tags) instead of client index and client fields
  - In extension 4b, the system will additionally check that the client will not have 2 HDB type properties after the edit on top of checking for duplicate property entry
    </box>

**Use Case 7: Filter Client**

Goal: Filter clients by name to quickly find specific clients

**MSS:**
1. Actor enters filter criteria (name or tag)
2. Actor uses the filterClient feature with the keywords
3. System filters the client list to show only clients whose names match the keywords
4. System displays the filtered list of clients

   Use case ends

**Extensions:**
* 2a. No keywords provided
    * 2a1. System shows error message requesting keywords
      Step 2a1 repeats until Actor provides keywords.
      Use case resumes from step 3

* 3a. No clients match the keywords
    * 3a1. System shows message that no clients match the criteria
      Use case ends

<box type="note" seamless>

**Note:**

- Filter Property works the same way, with the following differences:
    - In step 1, the actor can filter by property address, price range, or size range instead of client name and tag fields
      </box>
  
### Non-Functional Requirements

1. The application should work on any mainstream OS as long as it has Java 17 or above installed.
2. The system should be able to manage at least 1000 client records without noticeable degradation in performance during typical usage.
3. A user with above-average typing speed for regular English text should be able to complete most tasks faster using keyboard commands than using the mouse.
4. The system should respond to any valid user command within 2 seconds under normal operating conditions.
5. Error messages should be clear and guide the user towards corrective action.
6. The application should be packaged as a single executable JAR file.
7. User data should be stored locally and not transmitted externally without user consent.
8. The application should have a user-friendly command syntax that is consistent across different features.

### Glossary

* **Mainstream OS**: Windows, macOS, and Linux.
* **Executable JAR file**: A packaged Java archive file that can be run directly to start the application.
* **MSS**: Stands for Main Success Scenario, a scenario of the use case which will be considered a successful one.
* **Extension**: An alternative scenario that may occur during the execution of a use case, which may be triggered by an error or an edge case.
* **Client**: A person who is either buying or selling a property, whose details are stored in the address book.
* **Property**: A residential property listing, whose details are stored in the address book and linked to a client who owns it.
* **Index**: The position of a client or property in the displayed list, starting from 1 for the first item.
* **Filter criteria**: The keywords or parameters used to filter the client or property list (e.g., name keywords for clients, address keywords/price range/size range for properties).
* **Filtered list**: A subset of the full client or property list that matches the filter criteria and is displayed to the user.
* **Command syntax**: The format of the commands that the user types to interact with the application (e.g., `add n/John Doe p/98765432`)
* **Valid user command**: A command that follows the defined command syntax and can be parsed and executed by the application without errors.
* **Above-average typing speed**: A typing speed that is faster than 40 words per minute (wpm)

---

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder
   2. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.
2. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.
   2. Re-launch the app by double-clicking the jar file.<br>
      Expected: The most recent window size and location is retained.
3. _{ more test cases … }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.
   2. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.
   3. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.
   4. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.
2. _{ more test cases … }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_
2. _{ more test cases … }_

## **Appendix: Planned Enhancements** ##
Team size: 5

1. **List ordering**: Allow users to order the displayed list of clients or properties by different fields (e.g., name, price, size) using a command like `list orderby/price`. 
2. **Duplicate client error message**: Make the error message for adding a duplicate client more specific. Currently, attempting to add a client with an identical name produces the generic message `This person already exists in the address book.` We plan to update this to: `A client with the name [NAME] already exists`. If this is a different person, consider using a distinguishing middle name or suffix (e.g., `Alice Tan 2`).
3. **Duplicate phone number error message**: When adding a client with a phone number that already exists in the address book, the error message should specify which existing client has that phone number. For example: `The phone number [PHONE] is already associated with client [NAME].`
4. **Duplicate property error message**: When adding a property that already exists in the address book, the error message should specify which existing property has that address. For example: `A property with the address [ADDRESS] already exists, owned by client [NAME].`
5. **Out-of-range error message**: Make the error message for out-of-range index more descriptive. When a user types a command like `deleteProperty 99` and there are fewer than 99 contacts, the current error message is `Invalid property index!` We plan to improve this to: `Index 99 is out of range. The current list has [N] properties. Please enter an index between 1 and [N].`
6. **Improve the edit command output**: Make successful editClient show what was actually changed. Currently, a successful edit command returns `Edited Person: [all fields]`, which makes it hard to see what changed. We plan to update the success message to show only the fields that were modified: e.g., `Edited contact Alex Yeoh: phone updated from 91234567 to 98765432.` This can be extended to editProperty as well, e.g., `Edited property 1: price updated from $500,000 to $550,000.`