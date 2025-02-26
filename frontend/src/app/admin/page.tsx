import RequireAdmin from "@/components/auth/require-admin";
import ClientPage from "./client-page";

export default function AdminPage() {
  return (
    <RequireAdmin>
      <ClientPage />
    </RequireAdmin>
  );
}
