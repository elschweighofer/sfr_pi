package org.couchbase.quickstart.controllers;

import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryScanConsistency;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.couchbase.quickstart.models.Temperature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

import static org.couchbase.quickstart.configs.CollectionNames.TEMPERATURE;

@RestController
@RequestMapping("/api/v1/temperature")
public class TemperatureController {
    @Autowired
    DbController dbController;


    @CrossOrigin(value = "*")
    @PostMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create a user temperature from the request")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created", response = Temperature.class),
            @ApiResponse(code = 400, message = "Bad request", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public ResponseEntity<Temperature> save(@RequestBody final Temperature temperature) {
        try {
            dbController.saveEntity(temperature);
            return ResponseEntity.status(HttpStatus.CREATED).body(temperature);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @CrossOrigin(value = "*")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get a user temperature by id", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OK"),
                    @ApiResponse(code = 500, message = "Error occurred in getting user temperatures", response = Error.class)
            })
    public ResponseEntity<Temperature> gettemperature(@PathVariable("id") String id) {
        Temperature temperature = dbController.getEntity(id);
        return ResponseEntity.status(HttpStatus.OK).body(temperature);
    }

    @CrossOrigin(value = "*")
    @PutMapping(path = "/{id}")
    @ApiOperation(value = "Update a user temperature", response = Temperature.class)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Updated the user temperature", response = Temperature.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public ResponseEntity<Temperature> update(@PathVariable("id") String id, @RequestBody Temperature temperature) {
        try {
            dbController.updateEntity(id, temperature);
            return ResponseEntity.status(HttpStatus.CREATED).body(temperature);
        } catch (DocumentNotFoundException dnfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @CrossOrigin(value = "*")
    @DeleteMapping(path = "/{id}")
    @ApiOperation(value = "Delete a user temperature")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public ResponseEntity delete(@PathVariable String id) {

        try {
            dbController.deleteEntity(id);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (DocumentNotFoundException dnfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @CrossOrigin(value = "*")
    @GetMapping(path = "/temperatures/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Search for user temperatures", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Returns the list of user temperatures"),
                    @ApiResponse(code = 500, message = "Error occurred in getting user temperatures", response = Error.class)
            })
    public ResponseEntity<List<Temperature>> getTemperatures(
            @RequestParam(required = false, defaultValue = "5") int limit,
            @RequestParam(required = false, defaultValue = "0") int skip,
            @RequestParam String search) {

        List <Temperature> temperatures = dbController.getEntities(limit,skip, search);
        return ResponseEntity.status(HttpStatus.OK).body(temperatures);
    }
}
