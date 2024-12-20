import {Injectable, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {HttpErrorResponse} from "@angular/common/http";


@Injectable({
  providedIn: 'root'
})
export class GlobalVariables implements OnInit
{

  constructor(private toastrService: ToastrService,  private router: Router ) {
    const theme = localStorage.getItem('theme');
    if (theme === 'dark') {
      this.DarkMode()
    }else{
      this.Light()
    }
  }

  ngOnInit(): void {
    throw new Error('Method not implemented.');
  }

  DarkMode(){
    document.documentElement.style.setProperty(`--navarbarcolor`, '#1a1c23');
    document.documentElement.style.setProperty(`--white`, '#121317');
    document.documentElement.style.setProperty(`--icons`, '#ada4d9');
    document.documentElement.style.setProperty(`--SideBar`, '#1a1c23');
    document.documentElement.style.setProperty(`--shdow`, '0 4px 2px -2px rgba(255, 255, 255, 0.5)');
  }
  Light(){
    document.documentElement.style.setProperty(`--navarbarcolor`, '#fff');
    document.documentElement.style.setProperty(`--white`, '#fff');
    document.documentElement.style.setProperty(`--icons`, '#323993');
    document.documentElement.style.setProperty(`--SideBar`, '#323993');
    document.documentElement.style.setProperty(`--shdow`, '0 4px 6px -1px rgba(0, 0, 0, .1), 0 2px 4px -1px rgba(0, 0, 0, .06)');
  }
  handleHttpError(error: HttpErrorResponse): string {
    switch (error.status) {
      case 200: {
        return `Successfully`;
      }
      case 201: {
        return `Successfully`;
      }
      case 400: {
        // this.showMsg('there is some error → '+ error.error )
        return `Please check your request: ${error.message}`;
      }
      case 401: {
        this.router.navigate(['login'])
        // this.showMsg('Unauthorized , You Have to reconnect','bgcolor-error')
        return `Unauthorized: ${error.message}`;
      }
      case 403: {
        this.router.navigate(['login'])
        // this.showMsg('Forbidden , You Have to reconnect','bgcolor-error')
        return `Access Denied: ${error.message}`;
      }
      case 404: {
        if(error.url!.includes('/api/v1/message/')){
          return 'room not created yet'
        }
        else{
          // this.router.navigate(['404'])
          return `Access Denied: ${error.message}`;
        }
      }
      case 500: {
        // this.router.navigate(['500'])
        return `Internal Server Error: ${error.message}`;
      }
      case 502: {
        // this.router.navigate(['502'])
        return `Internal Server Error: ${error.message}`;
      }
      default: {
        //console.error(error)
        return `Unknown Server Error: ${error.message}`;
      }
    }
  }
  private messageCountMap: Map<string, number> = new Map();
  showMsg(message: string, duration?: number, action?: string) {


    // Check if the message has already appeared
    if (this.messageCountMap.has(message)) {
      // If it has, increment the count
      this.messageCountMap.set(message, this.messageCountMap.get(message)! + 1);
    } else {
      // If it's a new message, set count to 1
      this.messageCountMap.set(message, 1);
    }

    // Optionally, clear the message after a duration (default to 1500ms)
    const clearDuration = duration ? duration : 1500;

    // Delay the logging until the message is cleared
    setTimeout(() => {
      const count = this.messageCountMap.get(message);
      if (count !== undefined) {
        // Format the message with count only if count is greater than 1
        const formattedCount = count > 1 ? ` ( X${count} )` : '';
        const msg = `${message}${formattedCount}`;

        // Show toastr message based on action type
        switch (action) {
          case 'error':
            this.toastrService.error(msg);
            break;
          case 'info':
            this.toastrService.info(msg);
            break;
          case 'success':
            this.toastrService.success(msg);
            break;
          case 'warning':
            this.toastrService.warning(msg);
            break;
          default:
            this.toastrService.info(msg); // Default case if action is undefined or doesn't match any case
            break;
        }

      }

      // Delete the message from the map after the duration
      this.messageCountMap.delete(message);
    }, clearDuration);
  }

  getImg(file: any) {
    return new Promise<string | ArrayBuffer | null>((resolve, reject) => {
      const fileReader = new FileReader();
      fileReader.onload = () => resolve(fileReader.result);
      fileReader.readAsDataURL(file);
    });
  }

}
