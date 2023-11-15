import { Component, OnInit } from "@angular/core";
import { ERelationship } from "app/model/ERelationship";
import { Friend } from "app/model/Friend";
import { FriendService } from "app/services/friend.service";

@Component({
  selector: "app-request",
  templateUrl: "./request.component.html",
  styleUrls: ["./request.component.scss"],
})
export class RequestComponent implements OnInit {
  public requests: Friend[] = [];

  constructor(private _friendService: FriendService) {}

  ngOnInit(): void {
    this._friendService
      .search({
        page: 0,
        size: 10,
        username: "",
        relationship: ERelationship.REQUESTED,
      })
      .subscribe((res) => {
        this.requests = res.data.data;
      });
  }
}
