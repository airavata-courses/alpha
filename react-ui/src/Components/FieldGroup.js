import React from "react";
import {
  FormControl,
  FormGroup,
  ControlLabel,
  HelpBlock
} from "react-bootstrap";

export const FieldGroup = ({ id, label, help, ...props }) => {
  return (
    <FormGroup controlId={id}>
      <div style={{ display: "flex", justifyContent: "center" }}>
        <ControlLabel style={{ margin: "auto" }}>{label}</ControlLabel>
        <FormControl {...props} />
        {help && <HelpBlock>{help}</HelpBlock>}
      </div>
    </FormGroup>
  );
};
